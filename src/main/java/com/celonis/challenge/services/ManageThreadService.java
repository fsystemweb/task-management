package com.celonis.challenge.services;

import com.celonis.challenge.executor.CustomExecutor;
import com.celonis.challenge.model.ProjectGenerationTask;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ManageThreadService implements ManageThreadServiceInterface {
    private Set<TaskCounterRunnable> runnables = new HashSet<>();
    private ReentrantLock lock = new ReentrantLock();


    public CompletableFuture createNewThread(ProjectGenerationTask task) {
        lock.lock();
        try {
            TaskCounterRunnable thread = getThreadByName(task.getId());
            if (thread != null)
                return null;

            TaskCounterRunnable runnable = new TaskCounterRunnable(task);
            return executor(runnable);
        } finally {
            lock.unlock();
        }
    }

    private CompletableFuture executor(TaskCounterRunnable runnable) {
        runnables.add(runnable);

        CustomExecutor customExecutor = new CustomExecutor();


        return CompletableFuture.runAsync(runnable, customExecutor.getInstance())
                .thenRun(() -> {
                    runnables.remove(runnable);
                });
    }

    public int getThreadStatus(String taskId) {
        TaskCounterRunnable thread = getThreadByName(taskId);
        if (thread == null)
            return -1;

        return thread.getStatus();
    }

    public void stopThreadByTaskId(String taskId) {
        TaskCounterRunnable thread = getThreadByName(taskId);
        if (thread == null)
            return;

        thread.shutdown();
    }

    private TaskCounterRunnable getThreadByName(String threadName) {

        for (TaskCounterRunnable runnable : runnables) {
            if (runnable.getName().matches(threadName)) {
                return runnable;
            }
        }

        return null;
    }


}
