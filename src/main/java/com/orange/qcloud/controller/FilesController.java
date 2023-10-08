package com.orange.qcloud.controller;

import com.orange.qcloud.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FilesController {
    private final FilesService filesService;
}
