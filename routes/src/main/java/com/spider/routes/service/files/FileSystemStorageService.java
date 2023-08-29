package com.spider.routes.service.files;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;
import com.spider.routes.dto.RouteDto;
import com.spider.routes.dto.UserDto;
import com.spider.routes.exception.InvalidFormatException;
import com.spider.routes.exception.StorageException;
import com.spider.routes.exception.StorageFileNotFoundException;
import com.spider.routes.model.SpiderData;
import com.spider.routes.service.SpiderDataService;
import com.spider.routes.service.UserService;
import com.spider.routes.util.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    private final ResourceLoader resourceLoader;

    private final SpiderDataService spiderDataService;

    private final UserService userService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, ResourceLoader resourceLoader, SpiderDataService spiderDataService, UserService userService) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.resourceLoader = resourceLoader;
        this.spiderDataService = spiderDataService;
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
                Double.parseDouble(coordinates[0]);
                Double.parseDouble(coordinates[1]);
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

            SpiderData spiderData = spiderDataService.createSpiderData(userService.getUserById(userDto.getId()));

            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(spiderData.getProblemFilename())
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
    public void storeJsonAsFile(String filename, String json) {
        String destinationFile = this.rootLocation.resolve(
                Paths.get(filename)
        ).normalize().toAbsolutePath().toString();

        try (FileWriter writer = new FileWriter(destinationFile)) {
            // Write the JSON string to the file
            writer.write(json);
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
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public String loadAsSpiderRequestJson(String filename) throws IOException, InvalidFormatException {
        Resource resource = loadAsResource(filename);
        String fileContent = getResourceAsString(resource);
        return convertContentToSpiderFormat(fileContent, filename);
    }

    @Override
    public void deleteAll() {
        spiderDataService.deleteAllSpiderData();
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

    private String getResourceAsString(Resource resource) {
        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            return contentBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonObject getSpiderJsonTemplate() {
        Resource resource = resourceLoader.getResource("classpath:json_templates/template.json");
        String templateContent = getResourceAsString(resource);
        return new Gson().fromJson(templateContent, JsonObject.class);
    }

    private JsonObject createOrderJson(double latitude, double longitude, int i) {
        JsonObject json = new JsonObject();

        json.addProperty("id", i);
        json.addProperty("type", "delivery");

        JsonArray sizeArray = new JsonArray();
        sizeArray.add(1);
        json.add("size", sizeArray);

        JsonObject deliveryObject = new JsonObject();
        String address = String.format(Locale.US, "lat=%.7f;lon=%.7f", latitude, longitude);
        deliveryObject.addProperty("address", address);
        json.add("delivery", deliveryObject);
        return json;
    }

    private String convertContentToSpiderFormat(String content, String filename) throws InvalidFormatException, IOException {
        JsonArray ordersArray = new JsonArray();
        String[] lines = content.split("\n");

        // Check if each subsequent line has the correct format
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            String[] coordinates = line.split(",");

            if (coordinates.length != 2) {
                throw new InvalidFormatException("The file has incorrect format");
            }

            try {
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);
                JsonObject order = createOrderJson(latitude, longitude, i);
                ordersArray.add(order);
            } catch (NumberFormatException e) {
                throw new InvalidFormatException("The file has incorrect format");
            }
        }

        JsonObject template = getSpiderJsonTemplate();
        template.addProperty("id", filename);
        template.getAsJsonObject("vrp").add("orders", ordersArray);
        return template.toString();
    }

    @Override
    public void writeRouteDataToCsv(RouteDto routeDto, String filename) throws IOException {
        String destinationFile = this.rootLocation.resolve(
                Paths.get(filename + "_csv")
        ).normalize().toAbsolutePath().toString();
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(destinationFile))) {
            // Write header
            String[] header = {"Vehicle ID", "Type", "Order ID", "Start Time", "Duration"};
            csvWriter.writeNext(header);

            // Write route and visit data
            for (RouteDto.Route route : routeDto.getRoutes()) {
                String vehicleId = route.getVehicleId();
                for (RouteDto.Visit visit : route.getVisits()) {
                    String type = visit.getType();
                    String orderId = visit.getOrderId();
                    String startTime = visit.getStartTime();
                    String duration = visit.getDuration();
                    String[] row = {vehicleId, type, orderId, startTime, duration};
                    csvWriter.writeNext(row);
                }
            }
        }
    }
}
