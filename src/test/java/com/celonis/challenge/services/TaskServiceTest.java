package com.celonis.challenge.services;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.model.EnumStatusTask;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.model.ProjectGenerationTaskRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.celonis.challenge.Factories.FactoryTaskTest.*;
import static com.celonis.challenge.constants.Contants.PROJECT_FILENAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private TaskService taskService;

    @Mock
    private ProjectGenerationTaskRepository projectGenerationTaskRepository;
    @Mock
    private FileServiceInterface fileService;
    @Mock
    private ManageThreadServiceInterface manageThreadService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(projectGenerationTaskRepository, fileService, manageThreadService);
    }

    @Test
    void testListTasks_OK() {
        List<ProjectGenerationTask> taskListMock = generateTaskList();
        when(projectGenerationTaskRepository.findAll()).thenReturn(taskListMock);

        List<ProjectGenerationTask> taskList = taskService.listTasks();

        assertEquals(taskListMock, taskList);
    }

    @Test
    void testCreateTask_OK() {
        ProjectGenerationTask taskMock = generateTask();
        when(projectGenerationTaskRepository.save(any())).thenReturn(taskMock);

        ProjectGenerationTask task = taskService.createTask(new ProjectGenerationTask());

        assertEquals(taskMock, task);
    }

    @Test
    void testGetTask_OK() {
        ProjectGenerationTask taskMock = generateTask();
        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);

        ProjectGenerationTask task = taskService.getTask("MyTask1");

        assertEquals(taskMock, task);
    }


    @Test
    void testUpdateTask_OK() {
        ProjectGenerationTask taskMock = generateTask();

        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);

        when(projectGenerationTaskRepository.save(any())).thenReturn(taskMock);

        ProjectGenerationTask task = taskService.update("MyTask1", new ProjectGenerationTask());

        assertEquals(taskMock, task);
    }

    @Test
    void testDelete_OK() {
        doNothing().when(projectGenerationTaskRepository).deleteById("MyTask1");

        taskService.delete("MyTask1");

        verify(projectGenerationTaskRepository, times(1)).deleteById(any());
    }

    @Test
    void testExecuteTask_FailInternalException() {
        try {
            taskService.executeTask("MyTask1");
            Assert.fail("Expected exception to be thrown");
        } catch (InternalException e) {
            assertThat(e)
                    .isInstanceOf(InternalException.class);
        }
    }

    @Test
    void testExecuteTask_OK() throws IOException {
        ProjectGenerationTask taskMock = generateTask();

        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);

        doNothing().when(fileService).storeResult(any(), any());

        taskService.executeTask("MyTask1");

        verify(projectGenerationTaskRepository, times(1)).findById(any());
        verify(fileService, times(1)).storeResult(any(), any());
    }

    @Test
    void testExecuteCountTask_FailNullPointer() {
        ProjectGenerationTask taskMock = generateCountTask();

        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);

        try {
            taskService.executeTask("MyTask1");
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            verify(projectGenerationTaskRepository, times(1)).findById(any());
            verify(manageThreadService, times(1)).createNewThread(any());
        }
    }

    @Test
    void testExecuteCountTask_OK() {
        ProjectGenerationTask taskMock = generateCountTask();

        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);

        when(manageThreadService.createNewThread(any())).thenReturn(Mockito.mock(CompletableFuture.class));

        taskService.executeTask("MyTask1");


        verify(projectGenerationTaskRepository, times(1)).findById(any());
        verify(manageThreadService, times(1)).createNewThread(any());

    }

    @Test
    void testGetTaskResult_OK() {
        ProjectGenerationTask taskMock = generateTask();

        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);

        FileSystemResource fileSystemResourceMock = new FileSystemResource("src/test/resources/challenge.zip");

        when(fileService.getTaskResult(any())).thenReturn(fileSystemResourceMock);

        ResponseEntity result = taskService.getTaskResult("MyTask1");

        assertTrue(result.getBody().toString().contains(PROJECT_FILENAME));
        assertTrue(result.getStatusCode() == HttpStatus.OK);
    }

    @Test
    void testGetProgress_Completed() {
        ProjectGenerationTask taskMock = generateCountTask();

        taskMock.setStatus(EnumStatusTask.COMPLETED);

        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);


        int result = taskService.getTaskProgress("MyTask1");

        verify(manageThreadService, times(0)).getThreadStatus(any());
        assertEquals(taskMock.getEnd(), result);
    }

    @Test
    void testGetProgress_getThreadStatusHaveBeenCalled() {
        ProjectGenerationTask taskMock = generateCountTask();

        taskMock.setStatus(EnumStatusTask.NEW);

        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);

        taskService.getTaskProgress("MyTask1");

        verify(manageThreadService, times(1)).getThreadStatus(any());
    }

    @Test
    void testCancelTask_stopThreadByTaskIdHaveBeenCalled() {
        ProjectGenerationTask taskMock = generateCountTask();

        Optional<ProjectGenerationTask> optional = Optional.of(taskMock);

        when(projectGenerationTaskRepository.findById(any())).thenReturn(optional);


        taskService.cancelTask("MyTask1");

        verify(manageThreadService, times(1)).stopThreadByTaskId(any());
    }


}
