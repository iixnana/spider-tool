package com.spider.routes.service;

import com.spider.routes.dto.SpiderSessionDto;
import com.spider.routes.model.SpiderSession;
import com.spider.routes.repository.SpiderSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<SpiderSession> getSpiderSessionsWithoutOptimization() {
        return spiderSessionRepository.findByIsReadyIsTrueAndOptimizationIsRunningIsFalseAndIterationCountIs(0);
    }

    public List<SpiderSession> getSpiderSessionsNotReady() {
        return spiderSessionRepository.findByIsReadyIsFalse();
    }

    public List<SpiderSession> getSpiderSessionsWithRunningOptimization() {
        return spiderSessionRepository.findByOptimizationIsRunningIsTrue();
    }

    public List<SpiderSession> getSpiderSessionsWithCompletedOptimization() {
        return spiderSessionRepository.findByOptimizationIsRunningIsFalseAndIsReadyIsTrueAndBestSolutionValueIsNotNull();
    }

    public SpiderSession createSpiderSession(SpiderSessionDto session) {
        SpiderSession spiderSession = new SpiderSession(
                session.getId(),
                session.isReady(),
                session.getSetupProgress(),
                session.isOptimizationIsRunning(),
                session.getIterationCount(),
                session.getOptimizationTime(),
                session.getBestSolutionValue(),
                session.getErrorDuringSetup(),
                session.getInternalOptimizerError(),
                new ArrayList<>()
        );
        return spiderSessionRepository.save(spiderSession);
    }

    public SpiderSession updateSpiderSession(Long id, SpiderSessionDto session) {
        Optional<SpiderSession> optionalSession = spiderSessionRepository.findById(id);
        if (optionalSession.isPresent()) {
            SpiderSession spiderSession = optionalSession.get();


            spiderSession.setBestSolutionValue(session.getBestSolutionValue());
            spiderSession.setErrorDuringSetup(session.getErrorDuringSetup());
            spiderSession.setReady(session.isReady());
            spiderSession.setInternalOptimizerError(session.getInternalOptimizerError());
            spiderSession.setIterationCount(session.getIterationCount());
            spiderSession.setOptimizationIsRunning(session.isOptimizationIsRunning());
            spiderSession.setOptimizationTime(session.getOptimizationTime());
            spiderSession.setSetupProgress(session.getSetupProgress());

            //Save current solution to compare with for progress
            if (session.getBestSolutionValue() != 0) {
                List<Integer> previousSolutions = spiderSession.getSolutionValues();
                previousSolutions.add(session.getBestSolutionValue());
                spiderSession.setSolutionValues(previousSolutions);
            }

            return spiderSessionRepository.save(spiderSession);
        } else {
            throw new EntityNotFoundException("SpiderSession not found with id: " + id);
        }
    }
}
