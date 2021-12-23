package com.fsystem.taskmanagement.services;

import com.fsystem.taskmanagement.Factories.FactoryTaskTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ManageThreadServiceTest {

    ManageThreadServiceInterface manageThreadService;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        manageThreadService = new ManageThreadService();
    }

    @Test
    void testCreateNewThread_returnNull() {
        ReflectionTestUtils.setField(manageThreadService, "runnables", generateRunnable());
        assertEquals(null, manageThreadService.createNewThread(FactoryTaskTest.generateCountTask()));
    }

    @Test
    void testCreateNewThread_return() {
        CompletableFuture completableFuture = manageThreadService.createNewThread(FactoryTaskTest.generateCountTask());
        assertTrue(completableFuture != null);
        assertTrue(!completableFuture.isDone());
    }

    @Test
    void testGetThread_StatusNotFound() {
        assertEquals(-1, manageThreadService.getThreadStatus("task1"));
    }

    @Test
    void testGetThread_StatusHaveBeenCalled() {
        TaskCounterRunnable taskCounterRunnableMock = Mockito.mock(TaskCounterRunnable.class);

        Mockito.when(taskCounterRunnableMock.getName()).thenReturn("task1");
        Mockito.when(taskCounterRunnableMock.getStatus()).thenReturn(20);

        Set<TaskCounterRunnable> taskCounterRunnableSet = new HashSet<>();
        taskCounterRunnableSet.add(taskCounterRunnableMock);

        ReflectionTestUtils.setField(manageThreadService, "runnables", taskCounterRunnableSet);

        assertEquals(20, manageThreadService.getThreadStatus("task1"));
    }

    @Test
    void testGetTaskProgress_OK() {
        TaskCounterRunnable taskCounterRunnableMock = Mockito.mock(TaskCounterRunnable.class);

        Mockito.when(taskCounterRunnableMock.getName()).thenReturn("task1");
        Mockito.when(taskCounterRunnableMock.getStatus()).thenReturn(20);

        Set<TaskCounterRunnable> taskCounterRunnableSet = new HashSet<>();
        taskCounterRunnableSet.add(taskCounterRunnableMock);

        ReflectionTestUtils.setField(manageThreadService, "runnables", taskCounterRunnableSet);

        assertEquals(20, manageThreadService.getThreadStatus("task1"));
    }

    @Test
    void testGetThread_ShutdownHaveBeenCalled() {
        TaskCounterRunnable taskCounterRunnableMock = Mockito.mock(TaskCounterRunnable.class);

        Mockito.when(taskCounterRunnableMock.getName()).thenReturn("task1");

        Set<TaskCounterRunnable> taskCounterRunnableSet = new HashSet<>();
        taskCounterRunnableSet.add(taskCounterRunnableMock);

        ReflectionTestUtils.setField(manageThreadService, "runnables", taskCounterRunnableSet);

        manageThreadService.stopThreadByTaskId("task1");

        verify(taskCounterRunnableMock, atLeast(1)).shutdown();
    }

    private Set<TaskCounterRunnable> generateRunnable() {
        TaskCounterRunnable taskCounterRunnable = new TaskCounterRunnable(FactoryTaskTest.generateCountTask());
        Set<TaskCounterRunnable> taskCounterRunnableSet = new HashSet<>();
        taskCounterRunnableSet.add(taskCounterRunnable);
        return taskCounterRunnableSet;
    }
}
