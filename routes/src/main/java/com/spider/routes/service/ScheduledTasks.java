package com.spider.routes.service;

import com.google.gson.Gson;
import com.spider.routes.dto.SpiderSessionDto;
import com.spider.routes.exception.InvalidFormatException;
import com.spider.routes.model.SpiderData;
import com.spider.routes.model.SpiderSession;
import com.spider.routes.service.files.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

@Service
public class ScheduledTasks {

    private final SpiderDataService spiderDataService;
    private final SpiderService spiderService;
    private final SpiderSessionService spiderSessionService;
    private final StorageService storageService;

    Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    Gson gson = new Gson();

    public ScheduledTasks(SpiderDataService spiderDataService, SpiderService spiderService, SpiderSessionService spiderSessionService, StorageService storageService) {
        this.spiderDataService = spiderDataService;
        this.spiderService = spiderService;
        this.spiderSessionService = spiderSessionService;
        this.storageService = storageService;
    }

    @Scheduled(fixedRate = 60000) // 60,000 milliseconds = 1 minute
    public void startSessionsTask() throws IOException, InterruptedException, InvalidFormatException {
        logger.info("Running scheduled startSessionsTask.");
        List<SpiderData> collectedSpiderDataRows = spiderDataService.getSpiderDataWithoutSession();

        if (!collectedSpiderDataRows.isEmpty()) {
            int successfulCounter = 0;
            int failedCounter = 0;

            logger.info("startSessionsTask(): creating session for {} rows", collectedSpiderDataRows.size());
            for (SpiderData row : collectedSpiderDataRows) {
                HttpResponse<String> response = spiderService.createSession(row.getProblemFilename());

                if (response.statusCode() == HttpStatus.CREATED.value()) {
                    SpiderSessionDto parsedResponseBody = gson.fromJson(response.body(), SpiderSessionDto.class);
                    spiderSessionService.createSpiderSession(parsedResponseBody);
                    successfulCounter += 1;
                } else {
                    logger.warn("Failed to start a session for {}. Response.body(): {}", row.getProblemFilename(), response.body());
                    failedCounter += 1;
                }
            }
            logger.info("startSessionsTask(): created session for {} rows, failed {} rows", successfulCounter, failedCounter);
        }
    }

    @Scheduled(fixedRate = 60000) // 60,000 milliseconds = 1 minute
    public void startOptimizationTask() throws IOException, InterruptedException, InvalidFormatException {
        logger.info("Running scheduled startOptimizationTask.");
        List<SpiderSession> collectedSpiderSessions = spiderSessionService.getSpiderSessionsWithoutOptimization();

        if (!collectedSpiderSessions.isEmpty()) {
            int successfulCounter = 0;
            int failedCounter = 0;

            logger.info("startOptimizationTask(): creating session for {} rows", collectedSpiderSessions.size());
            for (SpiderSession row : collectedSpiderSessions) {
                HttpResponse<String> response = spiderService.startOptimization(row.getSessionId());

                if (response.statusCode() == HttpStatus.OK.value()) {
                    response = spiderService.getSession(row.getSessionId());
                    SpiderSessionDto parsedResponseBody = gson.fromJson(response.body(), SpiderSessionDto.class);
                    spiderSessionService.updateSpiderSession(row.getId(), parsedResponseBody);
                    successfulCounter += 1;
                } else {
                    logger.warn("Failed to start an optimization for {}. Response.body(): {}", row.getSessionId(), response.body());
                    failedCounter += 1;
                }
            }
            logger.info("startOptimizationTask(): created session for {} rows, failed {} rows", successfulCounter, failedCounter);
        }
    }

    private void stopOptimization(String sessionId, Long rowId, List<Integer> solutionValues) throws IOException, InterruptedException, InvalidFormatException {
        if (solutionValues.size() > 3) {
            Integer last = solutionValues.get(solutionValues.size() - 1);
            Integer secondToLast = solutionValues.get(solutionValues.size() - 2);
            Integer thirdToLast = solutionValues.get(solutionValues.size() - 3);

            // Stop optimization if last three checks the value has not changed
            if (Objects.equals(last, secondToLast) && Objects.equals(secondToLast, thirdToLast)) {
                HttpResponse<String> response = spiderService.stopOptimization(sessionId);
                if (response.statusCode() == HttpStatus.OK.value()) {
                    // Update session info again
                    response = spiderService.getSession(sessionId);
                    SpiderSessionDto parsedResponseBody = gson.fromJson(response.body(), SpiderSessionDto.class);
                    spiderSessionService.updateSpiderSession(rowId, parsedResponseBody);
                    logger.info("Successfully stopped optimization for {}", sessionId);
                } else {
                    logger.info("Failed to stop optimization for {}", sessionId);
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000) // 60,000 milliseconds = 1 minute
    public void checkSessionsTask() throws IOException, InterruptedException, InvalidFormatException {
        logger.info("Running scheduled checkSessionsTask.");
        List<SpiderSession> collectedSpiderSessions = spiderSessionService.getSpiderSessionsWithRunningOptimization();

        if (!collectedSpiderSessions.isEmpty()) {
            int successfulCounter = 0;
            int failedCounter = 0;

            logger.info("checkSessionsTask(): checking session for {} rows", collectedSpiderSessions.size());
            for (SpiderSession row : collectedSpiderSessions) {
                HttpResponse<String> response = spiderService.getSession(row.getSessionId());

                if (response.statusCode() == HttpStatus.OK.value()) {
                    SpiderSessionDto parsedResponseBody = gson.fromJson(response.body(), SpiderSessionDto.class);
                    SpiderSession spiderSession = spiderSessionService.updateSpiderSession(row.getId(), parsedResponseBody);

                    // Check if optimization needs to be stopped
                    stopOptimization(row.getSessionId(), row.getId(), spiderSession.getSolutionValues());

                    successfulCounter += 1;
                } else {
                    logger.warn("Failed to check on a session for {}. Response.body(): {}", row.getSessionId(), response.body());
                    failedCounter += 1;
                }
            }
            logger.info("checkSessionsTask(): checking session for {} rows, failed {} rows", successfulCounter, failedCounter);
        }
    }

    @Scheduled(fixedRate = 60000) // 60,000 milliseconds = 1 minute
    public void downloadBestSolutionsTask() throws IOException, InterruptedException, InvalidFormatException {
        logger.info("Running scheduled downloadBestSolutionsTask.");
        List<SpiderSession> collectedSpiderSessions = spiderSessionService.getSpiderSessionsWithCompletedOptimization();

        if (!collectedSpiderSessions.isEmpty()) {
            int successfulCounter = 0;
            int failedCounter = 0;

            logger.info("downloadBestSolutionsTask(): downloading best solutions for {} rows", collectedSpiderSessions.size());
            for (SpiderSession row : collectedSpiderSessions) {
                HttpResponse<String> response = spiderService.getBestSolution(row.getSessionId());

                if (response.statusCode() == HttpStatus.OK.value()) {
                    // RouteDto parsedResponseBody = gson.fromJson(response.body(), RouteDto.class);
                    // TODO: Parse data and do actions with it, like downloading individual routes, fetch data about unservicedReasons, etc.
                    SpiderData spiderData = spiderDataService.updateSpiderDataRowSolution(row.getSpiderData().getId());
                    storageService.storeJsonAsFile(spiderData.getSolutionFilename(), response.body());
                    successfulCounter += 1;
                } else {
                    logger.warn("Failed to download best solution for {}. Response.body(): {}", row.getSessionId(), response.body());
                    failedCounter += 1;
                }
            }
            logger.info("downloadBestSolutionsTask(): checking session for {} rows, failed {} rows", successfulCounter, failedCounter);
        }
    }
}
