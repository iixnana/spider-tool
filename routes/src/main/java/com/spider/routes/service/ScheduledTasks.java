package com.spider.routes.service;

import com.google.gson.Gson;
import com.spider.routes.exception.InvalidFormatException;
import com.spider.routes.model.SpiderData;
import com.spider.routes.util.SpiderSessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class ScheduledTasks {

    private final SpiderDataService spiderDataService;
    private final SpiderService spiderService;
    private final SpiderSessionService spiderSessionService;

    Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    Gson gson = new Gson();

    public ScheduledTasks(SpiderDataService spiderDataService, SpiderService spiderService, SpiderSessionService spiderSessionService) {
        this.spiderDataService = spiderDataService;
        this.spiderService = spiderService;
        this.spiderSessionService = spiderSessionService;
    }

    // This method will be scheduled to run every minute
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
                    SpiderSessionResponse parsedResponseBody = gson.fromJson(response.body(), SpiderSessionResponse.class);
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
}
