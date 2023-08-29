package com.spider.routes.service.files;

import com.spider.routes.dto.RouteDto;
import com.spider.routes.dto.UserDto;
import com.spider.routes.exception.InvalidFormatException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    void store(MultipartFile file, UserDto userDto);

    void storeJsonAsFile(String filename, String json);

    void writeRouteDataToCsv(RouteDto routeDto, String filename) throws IOException;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    String loadAsSpiderRequestJson(String filename) throws IOException, InvalidFormatException;

    void deleteAll();

}
