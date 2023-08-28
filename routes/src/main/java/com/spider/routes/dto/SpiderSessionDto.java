package com.spider.routes.dto;

public class SpiderSessionDto {
    private String id;
    private boolean isReady;
    private String setupProgress;
    private String errorDuringSetup;
    private boolean optimizationIsRunning;
    private int iterationCount;
    private String optimizationTime;
    private int bestSolutionValue;
    private String internalOptimizerError;

    public SpiderSessionDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
