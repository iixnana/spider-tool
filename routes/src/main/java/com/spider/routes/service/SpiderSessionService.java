package com.spider.routes.service;

import com.spider.routes.repository.SpiderSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class SpiderSessionService {
    private final SpiderSessionRepository spiderSessionRepository;

    public SpiderSessionService(SpiderSessionRepository spiderSessionRepository) {
        this.spiderSessionRepository = spiderSessionRepository;
    }


}
