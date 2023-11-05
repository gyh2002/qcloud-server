package com.orange.qcloud.service;

import com.orange.qcloud.dto.FileStatusDTO;
import com.orange.qcloud.dto.FilesDTO;
import com.orange.qcloud.request.CreateFileRequest;
import com.orange.qcloud.request.DeleteFileRequest;
import com.orange.qcloud.request.RenameFileRequest;
import com.orange.qcloud.request.UpdateFileRequest;

public interface FilesService {
    FilesDTO getAllFilesByUser(String email);

    String getFilesContentByUserAndId(String email, long filesId);

    FilesDTO createFileByUserAndPidAndName(String email, CreateFileRequest fileInfo);

    FilesDTO createFolderByUserAndPidAndName(String email, CreateFileRequest fileInfo);

    String deleteFileByUserAndIdAndPid(String email, DeleteFileRequest deleteInfo);

    String deleteFolderByUserAndIdAndPid(String email, DeleteFileRequest deleteInfo);

    String updateFileContentByUserAndId(String email, UpdateFileRequest updateInfo);

    FilesDTO renameFileOrFolderByUserAndId(String email, RenameFileRequest renameInfo);

    FileStatusDTO getAllFileStatusByUser(String email);
}
