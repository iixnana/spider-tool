package com.spider.routes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "spider_session")
public class SpiderSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "is_ready")
    private boolean isReady;

    @Column(name = "setup_progress")
    private String setupProgress;

    @Column(name = "optimization_is_running")
    private boolean optimizationIsRunning;

    @Column(name = "iteration_count")
    private int iterationCount;

    @Column(name = "optimization_time")
    private String optimizationTime;

    @Column(name = "best_solution_value")
    private int bestSolutionValue;

    @OneToOne(mappedBy = "session")
    private SpiderData spiderData;

    public SpiderSession() {
    }

    public SpiderSession(String sessionId, boolean isReady, String setupProgress, boolean optimizationIsRunning, int iterationCount, String optimizationTime, int bestSolutionValue) {
        this.sessionId = sessionId;
        this.isReady = isReady;
        this.setupProgress = setupProgress;
        this.optimizationIsRunning = optimizationIsRunning;
        this.iterationCount = iterationCount;
        this.optimizationTime = optimizationTime;
        this.bestSolutionValue = bestSolutionValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public String getSetupProgress() {
        return setupProgress;
    }

    public void setSetupProgress(String setupProgress) {
        this.setupProgress = setupProgress;
    }

    public boolean isOptimizationIsRunning() {
        return optimizationIsRunning;
    }

    public void setOptimizationIsRunning(boolean optimizationIsRunning) {
        this.optimizationIsRunning = optimizationIsRunning;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public String getOptimizationTime() {
        return optimizationTime;
    }

    public void setOptimizationTime(String optimizationTime) {
        this.optimizationTime = optimizationTime;
    }

    public int getBestSolutionValue() {
        return bestSolutionValue;
    }

    public void setBestSolutionValue(int bestSolutionValue) {
        this.bestSolutionValue = bestSolutionValue;
    }
}
