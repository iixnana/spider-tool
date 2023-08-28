package com.spider.routes.controller;

import com.spider.routes.dto.UserDto;
import com.spider.routes.model.SpiderFile;
import com.spider.routes.service.UserService;
import com.spider.routes.service.files.SpiderFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spider-files")
@CrossOrigin(origins = "http://localhost:3000")
public class SpiderFileController {
    private final SpiderFileService spiderFileService;
    private final UserService userService;

    public SpiderFileController(SpiderFileService spiderFileService, UserService userService) {
        this.spiderFileService = spiderFileService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<SpiderFile>> getAllFilesRecordsForUser(@AuthenticationPrincipal UserDto userDto) {
        List<SpiderFile> spiderFiles = spiderFileService.getSpiderFilesByAuthor(userService.getUserById(userDto.getId()));
        return ResponseEntity.ok(spiderFiles);
    }
}
