package com.orange.qcloud.service.impl;

import com.orange.qcloud.common.ApiException;
import com.orange.qcloud.common.EnumError;
import com.orange.qcloud.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${qcloud.image.root-path}")
    private String ROOT_PATH;
    @Value("${qcloud.host}")
    private String HOST;
    @Override
    public String saveImageAndGetUrl(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ApiException(EnumError.SAVE_IMAGE_FAILED);
        }
        String filename = saveImage(file);
        Path path = Paths.get(filename);
        String fileName = path.getFileName().toString();
        return "http://"+ HOST +"/image/download?filename=" + fileName;
    }

    @Override
    public Resource getImageResource(String filename) {
        Path filePath = Paths.get(ROOT_PATH, filename);
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new ApiException(EnumError.GET_IMAGE_FAILED);
        }
        return resource;
    }

    private String saveImage(MultipartFile file) {
        String folder = ROOT_PATH;

        if (file.isEmpty()) {
            throw new ApiException(EnumError.SAVE_IMAGE_FAILED);
        }
        Path path = Paths.get(file.getOriginalFilename());
        String oldFileName = path.getFileName().toString();
        String extension = "";
        String baseName = oldFileName;
        int dotIndex = oldFileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = oldFileName.substring(dotIndex);
            baseName = oldFileName.substring(0, dotIndex);
        }

        String uuid = String.valueOf(UUID.randomUUID());
        String newFileName = baseName + "_" + uuid + extension;
        String savePath = Paths.get(folder, newFileName).toString();
        File dest = new File(savePath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new ApiException(EnumError.SAVE_IMAGE_FAILED);
        }
        return newFileName;
    }
}
