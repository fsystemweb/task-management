package com.celonis.challenge.executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CustomExecutor {
    private ExecutorService executorService = null;


    private static int MAX_THREADS;

    @Value("${maximumThreads}")
    public void setMAX_THREADS(int maximumThreads) {
        MAX_THREADS = maximumThreads;
    }


    public synchronized ExecutorService getInstance() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(MAX_THREADS);
        }
        return executorService;
    }
}
