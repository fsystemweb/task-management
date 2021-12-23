package com.celonis.challenge.services;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.model.EnumStatusTask;
import com.celonis.challenge.model.EnumTypeTask;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.model.ProjectGenerationTaskRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.celonis.challenge.constants.Contants.PROJECT_FILENAME;

@Service
public class TaskService implements TaskServiceInterface {

    private final ProjectGenerationTaskRepository projectGenerationTaskRepository;

    private final FileServiceInterface fileService;

    private final ManageThreadServiceInterface manageThreadService;


    public TaskService(ProjectGenerationTaskRepository projectGenerationTaskRepository,
                       FileServiceInterface fileService,
                       ManageThreadServiceInterface manageThreadService) {
        this.projectGenerationTaskRepository = projectGenerationTaskRepository;
        this.fileService = fileService;
        this.manageThreadService = manageThreadService;
    }

    public List<ProjectGenerationTask> listTasks() {
        return projectGenerationTaskRepository.findAll();
    }

    public ProjectGenerationTask createTask(ProjectGenerationTask projectGenerationTask) {
        projectGenerationTask.setId(null);
        projectGenerationTask.setCreationDate(new Date());
        projectGenerationTask.setStatus(EnumStatusTask.NEW);
        if (projectGenerationTask.getType() == null)
            projectGenerationTask.setType(EnumTypeTask.GENERATE_SAMPLE_PROJECT);
        return projectGenerationTaskRepository.save(projectGenerationTask);
    }

    public ProjectGenerationTask getTask(String taskId) {
        return get(taskId);
    }

    public ProjectGenerationTask update(String taskId, ProjectGenerationTask projectGenerationTask) {
        ProjectGenerationTask existing = get(taskId);
        existing.setCreationDate(projectGenerationTask.getCreationDate());
        existing.setName(projectGenerationTask.getName());
        return projectGenerationTaskRepository.save(existing);
    }


    public void delete(String taskId) {
        projectGenerationTaskRepository.deleteById(taskId);
    }

    public void executeTask(String taskId) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(PROJECT_FILENAME);
        if (url == null) {
            throw new InternalException("Zip file not found");
        }
        try {
            ProjectGenerationTask task = get(taskId);
            fileService.storeResult(task, url);

            if (task.getType() == EnumTypeTask.COUNT_TASK) {
                updateStatus(task, EnumStatusTask.RUNNING);
                executeCountTask(task);
            } else {
                updateStatus(task, EnumStatusTask.COMPLETED);
            }

        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    public int getTaskProgress(String taskId) {
        ProjectGenerationTask task = get(taskId);

        if (task.getStatus() == EnumStatusTask.COMPLETED)
            return task.getEnd();

        return manageThreadService.getThreadStatus(task.getId());
    }

    public void cancelTask(String taskId) {
        ProjectGenerationTask task = get(taskId);

        manageThreadService.stopThreadByTaskId(task.getId());

        updateStatus(task, EnumStatusTask.CANCELLED);
    }

    public ResponseEntity<FileSystemResource> getTaskResult(String taskId) {

        FileSystemResource fileSystemResource = fileService.getTaskResult(get(taskId));

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        respHeaders.setContentDispositionFormData("attachment", PROJECT_FILENAME);

        return new ResponseEntity<>(fileSystemResource, respHeaders, HttpStatus.OK);
    }

    private ProjectGenerationTask get(String taskId) {
        Optional<ProjectGenerationTask> projectGenerationTask = projectGenerationTaskRepository.findById(taskId);
        return projectGenerationTask.orElseThrow(NotFoundException::new);
    }

    private void updateStatus(ProjectGenerationTask task, EnumStatusTask status) {
        task.setStatus(status);
        projectGenerationTaskRepository.save(task);
    }

    private void executeCountTask(ProjectGenerationTask task) {
        manageThreadService.createNewThread(task).thenRun(() -> {
                    ProjectGenerationTask checkTask = get(task.getId());

                    if (checkTask.getStatus() == EnumStatusTask.CANCELLED)
                        return;
                    updateStatus(task, EnumStatusTask.COMPLETED);
                }
        );
    }
}
