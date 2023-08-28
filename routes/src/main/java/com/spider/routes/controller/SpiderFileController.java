package com.spider.routes.controller;

import com.spider.routes.dto.UserDto;
import com.spider.routes.exception.InvalidFormatException;
import com.spider.routes.model.SpiderFile;
import com.spider.routes.service.SpiderFileService;
import com.spider.routes.service.UserService;
import com.spider.routes.service.files.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/spider-files")
@CrossOrigin(origins = "http://localhost:3000")
public class SpiderFileController {
    private final SpiderFileService spiderFileService;
    private final UserService userService;

    private final StorageService storageService;

    public SpiderFileController(SpiderFileService spiderFileService, UserService userService, StorageService storageService) {
        this.spiderFileService = spiderFileService;
        this.userService = userService;
        this.storageService = storageService;
    }

    @GetMapping
    public ResponseEntity<List<SpiderFile>> getAllFilesRecordsForUser(@AuthenticationPrincipal UserDto userDto) throws IOException, InvalidFormatException {
        List<SpiderFile> spiderFiles = spiderFileService.getSpiderFilesByAuthor(userService.getUserById(userDto.getId()));
        return ResponseEntity.ok(spiderFiles);
    }
}
