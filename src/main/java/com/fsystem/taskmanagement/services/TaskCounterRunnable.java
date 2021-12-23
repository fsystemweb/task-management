package com.fsystem.taskmanagement.services;

import com.fsystem.taskmanagement.model.ProjectGenerationTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskCounterRunnable implements Runnable {
    private ProjectGenerationTask task;
    private int status;
    private volatile boolean shutdown;

    private static final Logger logger = LoggerFactory.getLogger(ManageThreadService.class);

    public TaskCounterRunnable(ProjectGenerationTask task) {
        this.task = task;
    }

    @Override
    public void  run() {
        logger.info("TaskId: " + task.getId() + " is running");
        int index = task.getStart();

        while (!shutdown && index <= task.getEnd()) {
            try {
                status = index;
                Thread.sleep(1000);
                index++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("TaskId: " + task.getId() + " is completed");

    }

    public int getStatus() {
        return status;
    }
    public String getName() {
        return this.task.getId();
    }

    public void shutdown() {
        shutdown = true;
    }
}
