package com.fsystem.taskmanagement.executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CustomExecutor {
    private static ExecutorService executorService = null;

    private CustomExecutor(){}

    private static int MAX_THREADS;

    @Value("${maximumThreads}:10")
    public void setMAX_THREADS(int maximumThreads) {
        MAX_THREADS = maximumThreads;
    }


    public static synchronized ExecutorService getInstance() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(MAX_THREADS);
        }
        return executorService;
    }
}
