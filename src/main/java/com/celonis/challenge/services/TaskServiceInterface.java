package com.celonis.challenge.services;

import com.celonis.challenge.model.EnumStatusTask;
import com.celonis.challenge.model.ProjectGenerationTask;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskServiceInterface {
    List<ProjectGenerationTask> listTasks();

    ProjectGenerationTask createTask(ProjectGenerationTask projectGenerationTask);

    ProjectGenerationTask getTask(String taskId);

    ProjectGenerationTask update(String taskId, ProjectGenerationTask projectGenerationTask);

    void delete(String taskId);

    void executeTask(String taskId);

    int getTaskProgress(String taskId);

    void cancelTask(String taskId);

    ResponseEntity<FileSystemResource> getTaskResult(String taskId);
}
