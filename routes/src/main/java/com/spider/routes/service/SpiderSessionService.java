package com.spider.routes.service;

import com.spider.routes.model.SpiderSession;
import com.spider.routes.repository.SpiderSessionRepository;
import com.spider.routes.util.SessionCreationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpiderSessionService {
    private final SpiderSessionRepository spiderSessionRepository;

    public SpiderSessionService(SpiderSessionRepository spiderSessionRepository) {
        this.spiderSessionRepository = spiderSessionRepository;
    }

    public List<SpiderSession> getAllSpiderSessions() {
        return spiderSessionRepository.findAll();
    }

    public SpiderSession getSpiderSessionById(Long id) {
        return spiderSessionRepository.findById(id).orElse(null);
    }

    public SpiderSession createSpiderSession(SessionCreationResponse session) {
        SpiderSession spiderSession = new SpiderSession(session.getId(), session.isReady(), session.getSetupProgress(), session.isOptimizationIsRunning(), session.getIterationCount(), session.getOptimizationTime(), session.getBestSolutionValue());
        return spiderSessionRepository.save(spiderSession);
    }
}
