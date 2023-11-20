package com.orange.qcloud.service.impl;

import com.orange.qcloud.common.ApiException;
import com.orange.qcloud.common.EnumError;
import com.orange.qcloud.dao.FilesRepository;
import com.orange.qcloud.dao.UsersRepository;
import com.orange.qcloud.dto.FileStatusDTO;
import com.orange.qcloud.dto.FilesDTO;
import com.orange.qcloud.entity.Files;
import com.orange.qcloud.entity.Users;
import com.orange.qcloud.request.CreateFileRequest;
import com.orange.qcloud.request.DeleteFileRequest;
import com.orange.qcloud.request.RenameFileRequest;
import com.orange.qcloud.request.UpdateFileRequest;
import com.orange.qcloud.service.FilesService;
import com.orange.qcloud.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {
    @Value("${qcloud.file.root-path}")
    private String ROOT_PATH;

    private final FilesRepository filesRepository;
    private final UsersRepository usersRepository;

    @Override
    public FilesDTO getAllFilesByUser(String email) {
        Users users = usersRepository.findUsersByEmail(email);
        return FilesDTO.convertToDTO(filesRepository.getFilesByUserAndIsRoot(users, true));
    }

    @Override
    public String getFilesContentByUserAndId(String email, long filesId) {
        Users user = usersRepository.findUsersByEmail(email);
        Files file = filesRepository.getFilesByUserAndId(user, filesId);
        if (file == null || file.isFolder()) {
            throw new ApiException(EnumError.FILE_NOT_EXIST);
        }
        String content = FileUtils.readFileContent(Paths.get(ROOT_PATH).resolve(file.getPath()).toString());
        if (content == null) {
            throw new ApiException(EnumError.FILE_NOT_EXIST);
        }
        return content;
    }

    @Override
    public FilesDTO createFileByUserAndPidAndName(String email, CreateFileRequest fileInfo) {
        Users users = usersRepository.findUsersByEmail(email);
        Files parent = filesRepository.getFilesByUserAndId(users, fileInfo.getParentId());
        if (parent == null) {
            throw new ApiException(EnumError.FOLDER_NOT_EXIST);
        }
        boolean c = filesRepository.existsByIdAndChildrenName(fileInfo.getParentId(), fileInfo.getName());
        if (c) {
            throw new ApiException(EnumError.FILE_ALREADY_EXIST);
        }
        String newFileRelativePath = Paths.get(parent.getPath(), fileInfo.getName()).toString();
        String newFileFullPath = Paths.get(ROOT_PATH, newFileRelativePath).toString();
        boolean success = FileUtils.createFile(newFileFullPath);
        if (!success) {
            throw new ApiException(EnumError.CREATE_FILE_FAILED);
        }
        Files newFile = Files.builder()
                .name(fileInfo.getName())
                .isFolder(false)
                .path(newFileRelativePath)
                .user(users)
                .isRoot(false)
                .hash(FileUtils.getFileHash(newFileFullPath))
                .versionNo(0L)
                .build();
        parent.addChild(newFile);
        parent = filesRepository.save(parent);
        return FilesDTO.convertToDTO(
                parent.getChildren()
                        .stream().filter(child -> child.getName().equals(newFile.getName()))
                        .findFirst().get()
        );
    }

    @Override
    public FilesDTO createFolderByUserAndPidAndName(String email, CreateFileRequest fileInfo) {
        Users users = usersRepository.findUsersByEmail(email);
        Files parent = filesRepository.getFilesByUserAndId(users, fileInfo.getParentId());
        if (parent == null) {
            throw new ApiException(EnumError.FOLDER_NOT_EXIST);
        }
        boolean c = filesRepository.existsByIdAndChildrenName(fileInfo.getParentId(), fileInfo.getName());
        if (c) {
            throw new ApiException(EnumError.FOLDER_ALREADY_EXIST);
        }
        Files newFolder = Files.builder()
                .name(fileInfo.getName())
                .isFolder(true)
                .path(Paths.get(parent.getPath(), fileInfo.getName()).toString())
                .user(users)
                .isRoot(false)
                .hash("")
                .versionNo(0L)
                .children(new ArrayList<>())
                .build();
        parent.addChild(newFolder);
        boolean success = FileUtils.createDirectory(Paths.get(ROOT_PATH, newFolder.getPath()).toString());
        if (!success) {
            throw new ApiException(EnumError.CREATE_FOLDER_FAILED);
        }
        parent = filesRepository.save(parent);
        return FilesDTO.convertToDTO(
                parent.getChildren()
                        .stream().filter(child -> child.getName().equals(newFolder.getName()))
                        .findFirst().get()
        );
    }

    @Override
    public String deleteFileByUserAndIdAndPid(String email, DeleteFileRequest deleteInfo) {
        Users users = usersRepository.findUsersByEmail(email);
        Files parent = filesRepository.getFilesByUserAndId(users, deleteInfo.getParentId());
        if (parent == null) {
            throw new ApiException(EnumError.FOLDER_NOT_EXIST);
        }
        boolean c = filesRepository.existsByIdAndParentId(deleteInfo.getId(), deleteInfo.getParentId());
        if (!c) {
            throw new ApiException(EnumError.FILE_NOT_EXIST);
        }
        Files deleteFile = filesRepository.getReferenceById(deleteInfo.getId());
        parent.removeChild(deleteFile);
        boolean success = FileUtils.deleteFile(Paths.get(ROOT_PATH, parent.getPath(), deleteFile.getName()).toString());
        if (!success) {
            throw new ApiException(EnumError.DELETE_FILE_FAILED);
        }
        filesRepository.save(parent);
        return "Delete success";
    }

    @Override
    public String deleteFolderByUserAndIdAndPid(String email, DeleteFileRequest deleteInfo) {
        Users users = usersRepository.findUsersByEmail(email);
        Files parent = filesRepository.getFilesByUserAndId(users, deleteInfo.getParentId());
        if (parent == null) {
            throw new ApiException(EnumError.FOLDER_NOT_EXIST);
        }
        boolean c = filesRepository.existsByIdAndParentId(deleteInfo.getId(), deleteInfo.getParentId());
        if (!c) {
            throw new ApiException(EnumError.FOLDER_NOT_EXIST);
        }
        Files deleteFolder = filesRepository.getReferenceById(deleteInfo.getId());
        parent.removeChild(deleteFolder);
        boolean success = FileUtils.deleteDirectory(Paths.get(ROOT_PATH, parent.getPath(), deleteFolder.getName()).toString());
        if (!success) {
            throw new ApiException(EnumError.DELETE_FOLDER_FAILED);
        }
        filesRepository.save(parent);
        return "Delete success";
    }

    @Override
    public FilesDTO renameFileOrFolderByUserAndId(String email, RenameFileRequest renameInfo) {
        Users users = usersRepository.findUsersByEmail(email);
        Files file = filesRepository.getFilesByUserAndId(users, renameInfo.getId());
        if (file == null) {
            throw new ApiException(EnumError.FILE_NOT_EXIST);
        }
        boolean c = filesRepository.existsByIdAndChildrenName(renameInfo.getParentId(), renameInfo.getNewName());
        if (c) {
            throw new ApiException(EnumError.FILE_ALREADY_EXIST);
        }
        file.setName(renameInfo.getNewName());
        String oldPath = file.getPath();
        String newPath = Paths.get(file.getPath()).getParent().resolve(file.getName()).toString();
        file.setPath(newPath);
        boolean success = FileUtils.rename(
                Paths.get(ROOT_PATH, oldPath).toString(),
                Paths.get(ROOT_PATH, newPath).toString()
        );
        if (!success) {
            throw new ApiException(EnumError.RENAME_FILE_FAILED);
        }
        return FilesDTO.convertToDTO(filesRepository.save(file));
    }

    @Override
    public String updateFileContentByUserAndId(String email, UpdateFileRequest updateInfo) {
        Users users = usersRepository.findUsersByEmail(email);
        Files file = filesRepository.getFilesByUserAndId(users, updateInfo.getId());
        if (file == null) {
            throw new ApiException(EnumError.FILE_NOT_EXIST);
        }
        boolean success = FileUtils.updateFileContent(
                Paths.get(ROOT_PATH, file.getPath()).toString(),
                updateInfo.getContent()
        );
        if (!success) {
            throw new ApiException(EnumError.UPDATE_FILE_FAILED);
        }
        file.setHash(FileUtils.getFileHash(Paths.get(ROOT_PATH, file.getPath()).toString()));
        file.setVersionNo(file.getVersionNo() + 1);
        filesRepository.save(file);
        return "Update success";
    }

    @Override
    public FileStatusDTO getAllFileStatusByUser(String email) {
        Users users = usersRepository.findUsersByEmail(email);
        return FileStatusDTO.convertToDTO(filesRepository.getFilesByUserAndIsRoot(users, true));
    }
}
