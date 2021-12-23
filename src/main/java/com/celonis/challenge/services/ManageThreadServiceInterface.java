package com.celonis.challenge.services;

import com.celonis.challenge.model.ProjectGenerationTask;

import java.util.concurrent.CompletableFuture;

public interface ManageThreadServiceInterface {
    public CompletableFuture createNewThread(ProjectGenerationTask task);

    public int getThreadStatus(String taskId);

    public void stopThreadByTaskId(String taskId);

}
