package com.spider.routes.controller;

import com.spider.routes.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Health");
    }

    @GetMapping("/private")
    public ResponseEntity<String> health(@AuthenticationPrincipal UserDto user) {
        return ResponseEntity.ok("Health - Private");
    }

}
