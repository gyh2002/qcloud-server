package com.orange.qcloud.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String saveImageAndGetUrl(MultipartFile file);

    Resource getImageResource(String filename);
}
