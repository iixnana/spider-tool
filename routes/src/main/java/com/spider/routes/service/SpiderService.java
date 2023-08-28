package com.spider.routes.service;

import com.spider.routes.exception.InvalidFormatException;
import com.spider.routes.service.files.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SpiderService {
    @Value("${spider.base-url}")
    private String spiderBaseUrl;

    private final StorageService storageService;

    public SpiderService(StorageService storageService) {
        this.storageService = storageService;
    }

    public String checkServerStatus() throws IOException, InterruptedException {
        String url = spiderBaseUrl + "/api/v1/server";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }

    public HttpResponse<String> createSession(String problemFilename) throws IOException, InterruptedException, InvalidFormatException {
        String url = spiderBaseUrl + "/api/v1/sessions";
        HttpClient client = HttpClient.newHttpClient();

        // Build the JSON request body
        String jsonBody = storageService.loadAsSpiderRequestJson(problemFilename);
        System.out.println(jsonBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void checkSession(String sessionId) {
        String url = spiderBaseUrl + "/api/v1/sessions/" + sessionId;
        HttpClient client = HttpClient.newHttpClient();
    }


}
