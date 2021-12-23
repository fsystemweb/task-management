package com.fsystem.taskmanagement.services;

import com.fsystem.taskmanagement.model.ProjectGenerationTask;

import java.util.concurrent.CompletableFuture;

public interface ManageThreadServiceInterface {
    public CompletableFuture createNewThread(ProjectGenerationTask task);

    public int getThreadStatus(String taskId);

    public void stopThreadByTaskId(String taskId);

}
