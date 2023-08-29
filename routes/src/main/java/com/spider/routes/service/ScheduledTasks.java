package com.spider.routes.service;

import com.google.gson.Gson;
import com.spider.routes.dto.RouteDto;
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

    @Scheduled(fixedRate = 1800000) // Every 30 minutes
    public void checkServerHealth() throws IOException, InterruptedException {
        logger.info("Checking spider health");
        HttpResponse<String> response = spiderService.checkServerStatus();
        logger.info("Spider status: {}", response.statusCode());
    }

    @Scheduled(fixedRate = 60000) // Every 1 minute
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
                    SpiderSession session = spiderSessionService.createSpiderSession(parsedResponseBody);
                    spiderDataService.updateSpiderDataRowSession(row.getId(), session);
                    successfulCounter += 1;
                } else {
                    logger.warn("Failed to start a session for {}. Response.body(): {}", row.getProblemFilename(), response.body());
                    failedCounter += 1;
                }
            }
            logger.info("startSessionsTask(): created session for {} rows, failed {} rows", successfulCounter, failedCounter);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void updateSessionsNotReadyTask() throws IOException, InterruptedException, InvalidFormatException {
        logger.info("Running scheduled updateSessionsNotReadyTask.");
        List<SpiderSession> collectedSpiderSessions = spiderSessionService.getSpiderSessionsNotReady();

        if (!collectedSpiderSessions.isEmpty()) {
            int successfulCounter = 0;
            int failedCounter = 0;

            logger.info("updateSessionsNotReadyTask(): checking session for {} rows", collectedSpiderSessions.size());
            for (SpiderSession row : collectedSpiderSessions) {
                HttpResponse<String> response = spiderService.getSession(row.getSessionId());

                if (response.statusCode() == HttpStatus.OK.value()) {
                    SpiderSessionDto parsedResponseBody = gson.fromJson(response.body(), SpiderSessionDto.class);
                    SpiderSession updatedSpiderSession = spiderSessionService.updateSpiderSession(row.getId(), parsedResponseBody);
                    if (updatedSpiderSession.isReady()) {
                        startOptimizationTask(row.getSessionId());
                        spiderSessionService.updateSpiderSessionAwaitingOptimization(row.getId(), true);
                    }
                    successfulCounter += 1;
                } else {
                    logger.warn("Failed to check on a session for {}. Response.body(): {}", row.getSessionId(), response.body());
                    failedCounter += 1;
                }
            }
            logger.info("updateSessionsNotReadyTask(): checking session for {} rows, failed {} rows", successfulCounter, failedCounter);
        }
    }

    private boolean startOptimizationTask(String sessionId) throws IOException, InterruptedException, InvalidFormatException {
        logger.info("Session is ready, starting optimization");
        HttpResponse<String> response = spiderService.startOptimization(sessionId);

        if (response.statusCode() == HttpStatus.OK.value()) {
            return true;
        } else {
            // TODO: automate re-try in case it fails to start
            logger.warn("Failed to start optimization for {}. Response status: {}", sessionId, response.statusCode());
        }
        return false;
    }

    private void stopOptimization(String sessionId, Long rowId, List<Double> solutionValues) throws IOException, InterruptedException, InvalidFormatException {
        if (solutionValues.size() > 3) {
            // Looking for more significant change
            Double last = (double) Math.round(solutionValues.get(solutionValues.size() - 1));
            Double secondToLast = (double) Math.round(solutionValues.get(solutionValues.size() - 2));
            Double thirdToLast = (double) Math.round(solutionValues.get(solutionValues.size() - 3));

            // Stop optimization if last three checks the value has not changed
            if (Objects.equals(last, secondToLast) && Objects.equals(secondToLast, thirdToLast)) {
                HttpResponse<String> response = spiderService.stopOptimization(sessionId);
                if (response.statusCode() == HttpStatus.OK.value()) {
                    // Update session info again
                    response = spiderService.getSession(sessionId);
                    SpiderSessionDto parsedResponseBody = gson.fromJson(response.body(), SpiderSessionDto.class);
                    spiderSessionService.updateSpiderSession(rowId, parsedResponseBody);
                    spiderSessionService.updateSpiderSessionAwaitingOptimization(rowId, false);
                    logger.info("Successfully stopped optimization for {}", sessionId);
                } else {
                    logger.info("Failed to stop optimization for {}", sessionId);
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000)
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
                } else if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
                    // Restart session
                    response = spiderService.createSession(row.getSpiderData().getProblemFilename());

                    if (!(response.statusCode() == HttpStatus.CREATED.value())) {
                        logger.warn("Failed to re-start a session with id: {}. Response.body(): {}", row.getSessionId(), response.body());
                    }
                } else {
                    logger.warn("Failed to check on a session for {}. Response.body(): {}", row.getSessionId(), response.body());
                    failedCounter += 1;
                }
            }
            logger.info("checkSessionsTask(): checking session for {} rows, failed {} rows", successfulCounter, failedCounter);
        }
    }

    @Scheduled(fixedRate = 60000)
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
                    // Process data and store as csv
                    RouteDto parsedResponseBody = gson.fromJson(response.body(), RouteDto.class);
                    storageService.writeRouteDataToCsv(parsedResponseBody, row.getSessionId());
                    // Update spider data with solution filename
                    SpiderData spiderData = spiderDataService.updateSpiderDataRowSolution(row.getSpiderData().getId());
                    storageService.storeJsonAsFile(spiderData.getSolutionFilename(), response.body());
                    // Delete session
                    spiderSessionService.updateSpiderSessionDownloadedSolution(row.getId(), true);
                    spiderService.deleteSession(row.getSessionId());
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
