package com.spider.routes.controller;

import com.spider.routes.service.SpiderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.http.HttpResponse;

// TODO: Build API endpoints so Spider management could be accessed from frontend

@RestController
@RequestMapping("/api/spider")
@CrossOrigin(origins = "http://localhost:3000")
public class SpiderController {
    private final SpiderService spiderService;

    public SpiderController(SpiderService spiderService) {
        this.spiderService = spiderService;
    }

    @GetMapping
    public ResponseEntity<String> getServerStatus() throws IOException, InterruptedException {
        HttpResponse<String> response = spiderService.checkServerStatus();
        return ResponseEntity.ok(response.body());
    }
}
