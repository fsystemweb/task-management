package com.fsystem.taskmanagement.services;

import com.fsystem.taskmanagement.model.EnumStatusTask;
import com.fsystem.taskmanagement.model.ProjectGenerationTask;
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
