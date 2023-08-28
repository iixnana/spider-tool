package com.spider.routes.service.files;

import com.spider.routes.dto.UserDto;
import com.spider.routes.exception.StorageException;
import com.spider.routes.exception.StorageFileNotFoundException;
import com.spider.routes.model.SpiderFile;
import com.spider.routes.service.UserService;
import com.spider.routes.util.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    private final SpiderFileService spiderFileService;

    private final UserService userService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, SpiderFileService spiderFileService, UserService userService) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.spiderFileService = spiderFileService;
        this.userService = userService;
    }

    private static boolean validateContent(String content) {
        String[] lines = content.split("\n");

        // Check if the first line matches the pattern
        String expectedFirstLine = "latitude,longitude";
        if (!lines[0].trim().equals(expectedFirstLine)) {
            return false;
        }

        // Check if each subsequent line has the correct format
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            String[] coordinates = line.split(",");

            if (coordinates.length != 2) {
                return false; // Each line should have exactly two coordinates
            }

            try {
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);
            } catch (NumberFormatException e) {
                return false; // Unable to parse coordinates as numbers
            }
        }

        return true;
    }

    @Override
    public void store(MultipartFile file, UserDto userDto) {
        try {
            // Do not store empty files
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            // Validate file structure
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            boolean isValid = validateContent(content);

            if (!isValid) {
                throw new StorageException("The file structure is invalid.");
            }

            SpiderFile spiderFile = spiderFileService.createSpiderFile(userService.getUserById(userDto.getId()));

            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(spiderFile.getProblemFilename())
            ).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // Security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        spiderFileService.deleteAllSpiderFiles();
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
