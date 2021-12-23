package com.celonis.challenge.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class ScheduledService {
    private static final Logger logger = LoggerFactory.getLogger(ManageThreadService.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Value("${taskCleanUp.ageMoreThan}")
    private long maxDiff;

    private TaskServiceInterface taskService;

    public ScheduledService(TaskServiceInterface taskService){
        this.taskService = taskService;
    }

    @Scheduled(cron = "${taskCleanUp.every}")
    public void cleanTask() {
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));

        LocalDateTime currentDateTime = LocalDateTime.now();

        taskService.listTasks().stream().forEach(task ->{
            long diff = ChronoUnit.SECONDS.between(dateToLocalDateTime(task.getCreationDate()), currentDateTime);
            if(diff >= maxDiff)
                taskService.delete(task.getId());
        });

        logger.info("Cron Task :: Tasks cleaned up  - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }

    private LocalDateTime dateToLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
