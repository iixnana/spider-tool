package com.spider.routes.model;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "spider_session")
public class SpiderSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "is_ready")
    private Boolean isReady;

    @Column(name = "setup_progress")
    private String setupProgress;

    @Column(name = "optimization_is_running")
    private Boolean optimizationIsRunning;

    @Column(name = "iteration_count")
    private Integer iterationCount;

    @Column(name = "optimization_time")
    private String optimizationTime;

    @Column(name = "best_solution_value")
    private Integer bestSolutionValue;

    @Column(name = "error_during_setup")
    private String errorDuringSetup;

    @Column(name = "internal_optimizer_error")
    private String internalOptimizerError;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "solution_values")
    private List<Integer> solutionValues;

    @OneToOne(mappedBy = "session")
    private SpiderData spiderData;

    @Column(name = "started_optimization")
    private Boolean isAwaitingOptimization;

    public SpiderSession() {
    }

    public SpiderSession(String sessionId, boolean isReady, String setupProgress, boolean optimizationIsRunning, int iterationCount, String optimizationTime, int bestSolutionValue, String errorDuringSetup, String internalOptimizerError, List<Integer> solutionValues, Boolean isAwaitingOptimization) {
        this.sessionId = sessionId;
        this.isReady = isReady;
        this.setupProgress = setupProgress;
        this.optimizationIsRunning = optimizationIsRunning;
        this.iterationCount = iterationCount;
        this.optimizationTime = optimizationTime;
        this.bestSolutionValue = bestSolutionValue;
        this.errorDuringSetup = errorDuringSetup;
        this.internalOptimizerError = internalOptimizerError;
        this.solutionValues = solutionValues;
        this.isAwaitingOptimization = isAwaitingOptimization;
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

    public String getErrorDuringSetup() {
        return errorDuringSetup;
    }

    public void setErrorDuringSetup(String errorDuringSetup) {
        this.errorDuringSetup = errorDuringSetup;
    }

    public String getInternalOptimizerError() {
        return internalOptimizerError;
    }

    public void setInternalOptimizerError(String internalOptimizerError) {
        this.internalOptimizerError = internalOptimizerError;
    }

    public List<Integer> getSolutionValues() {
        return solutionValues;
    }

    public void setSolutionValues(List<Integer> solutionValues) {
        this.solutionValues = solutionValues;
    }

    public SpiderData getSpiderData() {
        return spiderData;
    }

    public void setSpiderData(SpiderData spiderData) {
        this.spiderData = spiderData;
    }

    public Boolean getAwaitingOptimization() {
        return isAwaitingOptimization;
    }

    public void setAwaitingOptimization(Boolean awaitingOptimization) {
        isAwaitingOptimization = awaitingOptimization;
    }
}
