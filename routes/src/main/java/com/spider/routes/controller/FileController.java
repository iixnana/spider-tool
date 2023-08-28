package com.spider.routes.controller;

import com.spider.routes.dto.UserDto;
import com.spider.routes.exception.StorageFileNotFoundException;
import com.spider.routes.service.files.StorageService;
import com.spider.routes.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    private final StorageService storageService;

    @Autowired
    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public ResponseEntity<String[]> getListFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(storageService.loadAll().map(path -> path.getFileName().toString()).toArray(String[]::new));
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public ResponseEntity<Response> handleFilesUpload(@RequestParam("files") MultipartFile[] files, @AuthenticationPrincipal UserDto userDto) {
        try {
            List<String> fileNames = new ArrayList<>();

            Arrays.asList(files).forEach(file -> {
                storageService.store(file, userDto);
                fileNames.add(file.getOriginalFilename());
            });

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Response("Files uploaded successfully: " + fileNames));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new Response("Exception occurred while uploading files"));
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
