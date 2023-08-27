package com.spider.routes.controller;

import com.spider.routes.util.FileUploadResponse;
import com.spider.routes.util.FileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            createDirIfNotExist();

            List<String> fileNames = new ArrayList<>();

            // read and write the file to the local folder
            Arrays.asList(files).stream().forEach(file -> {
                byte[] bytes = new byte[0];
                try {
                    bytes = file.getBytes();
                    Files.write(Paths.get(FileUtil.folderPath + file.getOriginalFilename()), bytes);
                    fileNames.add(file.getOriginalFilename());
                } catch (IOException ignored) {

                }
            });

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new FileUploadResponse("Files uploaded successfully: " + fileNames));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new FileUploadResponse("Exception to upload files!"));
        }
    }

    private void createDirIfNotExist() {
        //create directory to save the files
        File directory = new File(FileUtil.folderPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    @GetMapping("/files")
    public ResponseEntity<String[]> getListFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(new File(FileUtil.folderPath).list());
    }
}
