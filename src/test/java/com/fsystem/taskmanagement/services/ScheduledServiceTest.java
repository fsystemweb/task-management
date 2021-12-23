package com.fsystem.taskmanagement.services;
import com.fsystem.taskmanagement.TaskManagementApplication;
import com.fsystem.taskmanagement.model.ProjectGenerationTask;
import org.awaitility.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static com.fsystem.taskmanagement.Factories.FactoryTaskTest.generateTaskList;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;


@SpringJUnitConfig(TaskManagementApplication.class)
@SpringBootTest
public class ScheduledServiceTest {

    @SpyBean
    private ScheduledService scheduledService;

    private ScheduledService service;

    @Mock
    private TaskServiceInterface taskService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        service = new ScheduledService(taskService);
    }

    @Test
    public void testCleanTask_HaveBeenCalledAfterOneSecond() {
        await()
                .atMost(Duration.ONE_SECOND)
                .untilAsserted(() -> verify(scheduledService, atLeast(1)).cleanTask());
    }


    @Test
    public void testCleanTask_DeleteHaveBeenCalled() {
        List<ProjectGenerationTask> taskListMock = generateTaskList();
        when(taskService.listTasks()).thenReturn(taskListMock);

        doNothing().when(taskService).delete(any());

        service.cleanTask();

        verify(taskService, atLeast(2)).delete(any());
    }
}
