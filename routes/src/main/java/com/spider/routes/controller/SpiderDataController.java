package com.spider.routes.controller;

import com.spider.routes.model.SpiderData;
import com.spider.routes.service.SpiderDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/spider-data")
@CrossOrigin(origins = "http://localhost:3000")
public class SpiderDataController {
    private final SpiderDataService spiderDataService;

    public SpiderDataController(SpiderDataService spiderDataService) {
        this.spiderDataService = spiderDataService;
    }

    @GetMapping
    public ResponseEntity<List<SpiderData>> getAllFiles() {
        List<SpiderData> spiderData = spiderDataService.getSpiderData();
        return ResponseEntity.ok(spiderData);
    }
}
