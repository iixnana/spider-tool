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

// TODO: Move API endpoint strings to separate file or application.properties
@Service
public class SpiderService {
    @Value("${spider.base-url}")
    private String spiderBaseUrl;

    private final StorageService storageService;

    public SpiderService(StorageService storageService) {
        this.storageService = storageService;
    }

    public HttpResponse<String> checkServerStatus() throws IOException, InterruptedException {
        String url = spiderBaseUrl + "/api/v1/server";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> checkSession(String sessionId) throws IOException, InterruptedException {
        String url = spiderBaseUrl + "/api/v1/sessions/" + sessionId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> checkSessionWarnings(String sessionId) throws IOException, InterruptedException {
        String url = spiderBaseUrl + "/api/v1/sessions/" + sessionId + "/warnings";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getBestSolution(String sessionId) throws IOException, InterruptedException {
        String url = spiderBaseUrl + "/api/v1/sessions/" + sessionId + "/bestSolution";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getBestSolutionRoute(String sessionId, String vId) throws IOException, InterruptedException {
        String url = spiderBaseUrl + "/api/v1/sessions/" + sessionId + "/bestSolution/routes/" + vId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getUnservicedReasonForOrder(String sessionId, String orderId) throws IOException, InterruptedException {
        String url = spiderBaseUrl + "/api/v1/sessions/" + sessionId + "/unservicedReason/" + orderId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> createSession(String problemFilename) throws IOException, InterruptedException, InvalidFormatException {
        String url = spiderBaseUrl + "/api/v1/sessions";
        HttpClient client = HttpClient.newHttpClient();

        // Build the JSON request body
        String jsonBody = storageService.loadAsSpiderRequestJson(problemFilename);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> startOptimization(String sessionId) throws IOException, InterruptedException, InvalidFormatException {
        String url = spiderBaseUrl + "/api/v1/sessions/" + sessionId + "/startOptimization";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(null)
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> stopOptimization(String sessionId) throws IOException, InterruptedException, InvalidFormatException {
        String url = spiderBaseUrl + "/api/v1/sessions/" + sessionId + "/stopOptimization";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(null)
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
