package com.orange.qcloud.controller;

import com.orange.qcloud.common.ApiResponse;
import com.orange.qcloud.dto.FileStatusDTO;
import com.orange.qcloud.dto.FilesDTO;
import com.orange.qcloud.request.CreateFileRequest;
import com.orange.qcloud.request.DeleteFileRequest;
import com.orange.qcloud.request.RenameFileRequest;
import com.orange.qcloud.request.UpdateFileRequest;
import com.orange.qcloud.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FilesController {
    private final FilesService filesService;

    @GetMapping("/get-all-file")
    public ApiResponse<FilesDTO> getAllFiles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FilesDTO res = filesService.getAllFilesByUser(auth.getName());
        return ApiResponse.success(res);
    }

    @GetMapping("/get-file-content")
    public ApiResponse<String> getFilesContent(@RequestParam long fileId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String content = filesService.getFilesContentByUserAndId(auth.getName(), fileId);
        return ApiResponse.success(content);
    }

    @PostMapping("/create-file")
    public ApiResponse<FilesDTO> createFile(@RequestBody CreateFileRequest fileInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FilesDTO res = filesService.createFileByUserAndPidAndName(auth.getName(), fileInfo);
        return ApiResponse.success(res);
    }

    @PostMapping("/create-folder")
    public ApiResponse<FilesDTO> createFolder(@RequestBody CreateFileRequest fileInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FilesDTO res = filesService.createFolderByUserAndPidAndName(auth.getName(), fileInfo);
        return ApiResponse.success(res);
    }

    @PostMapping("/delete-file")
    public ApiResponse<String> deleteFile(@RequestBody DeleteFileRequest deleteInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String res = filesService.deleteFileByUserAndIdAndPid(auth.getName(), deleteInfo);
        return ApiResponse.success(res);
    }

    @PostMapping("/delete-folder")
    public ApiResponse<String> deleteFolder(@RequestBody DeleteFileRequest deleteInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String res = filesService.deleteFolderByUserAndIdAndPid(auth.getName(), deleteInfo);
        return ApiResponse.success(res);
    }

    @PostMapping("/rename-file-folder")
    public ApiResponse<FilesDTO> renameFile(@RequestBody RenameFileRequest renameInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FilesDTO res = filesService.renameFileOrFolderByUserAndId(auth.getName(), renameInfo);
        return ApiResponse.success(res);
    }

    @PostMapping("/update-file")
    public ApiResponse<String> updateFile(@RequestBody UpdateFileRequest updateInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String res = filesService.updateFileContentByUserAndId(auth.getName(), updateInfo);
        return ApiResponse.success(res);
    }

    @GetMapping("/get-files-status")
    public ApiResponse<FileStatusDTO> getFilesStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        FileStatusDTO res = filesService.getAllFileStatusByUser(auth.getName());
        return ApiResponse.success(res);
    }
}
