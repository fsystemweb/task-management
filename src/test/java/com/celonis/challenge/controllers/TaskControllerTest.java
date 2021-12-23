package com.celonis.challenge.controllers;


import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.celonis.challenge.Factories.FactoryTaskTest.*;
import static com.celonis.challenge.constants.Contants.CELONIS_HEADER_KEY;
import static com.celonis.challenge.constants.Contants.CELONIS_HEADER_VALUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    TaskService taskService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testError_401() throws Exception {
        List<ProjectGenerationTask> taskListMock = generateTaskList();
        when(taskService.listTasks()).thenReturn(taskListMock);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tasks/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListTasks_OK() throws Exception {
        List<ProjectGenerationTask> taskListMock = generateTaskList();
        when(taskService.listTasks()).thenReturn(taskListMock);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tasks/")
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    void testCreateTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateTask();
        when(taskService.createTask(any())).thenReturn(taskMock);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/tasks/")
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .content(mapper.writeValueAsString(new ProjectGenerationTask()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(taskMock.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(taskMock.getName()));
    }

    @Test
    void testCreateCountTask_Fail() throws Exception {
        ProjectGenerationTask taskMock = generateCountTask();
        taskMock.setStart(100);
        taskMock.setEnd(0);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/tasks/")
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .content(mapper.writeValueAsString(taskMock))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists());
    }

    @Test
    void testCreateCountTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateCountTask();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/tasks/")
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .content(mapper.writeValueAsString(taskMock))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateCountTask();
        when(taskService.getTask(any())).thenReturn(taskMock);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tasks/" + taskMock.getId())
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(taskMock.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(taskMock.getName()));
    }

    @Test
    void testUpdateTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateTask();
        when(taskService.update(any(), any())).thenReturn(taskMock);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/tasks/" + taskMock.getId())
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .content(mapper.writeValueAsString(new ProjectGenerationTask()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(taskMock.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(taskMock.getName()));
    }

    @Test
    void testDeleteTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateCountTask();
        doNothing().when(taskService).delete(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/tasks/" + taskMock.getId())
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testExecuteTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateCountTask();
        doNothing().when(taskService).executeTask(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/tasks/" + taskMock.getId() + "/execute")
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetResultTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateTask();

        ResponseEntity responseEntityMock = new ResponseEntity<>(HttpStatus.OK);

        when(taskService.getTaskResult(any())).thenReturn(responseEntityMock);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tasks/" + taskMock.getId() + "/result")
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testCancelTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateCountTask();
        doNothing().when(taskService).cancelTask(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/tasks/" + taskMock.getId()+"/cancel")
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetProgressTask_OK() throws Exception {
        ProjectGenerationTask taskMock = generateCountTask();
        when(taskService.getTaskProgress(any())).thenReturn(20);

        Map<String, Integer> expectedValue = new HashMap<>();

        expectedValue.put("status", 20);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tasks/" + taskMock.getId()+"/status")
                        .header(CELONIS_HEADER_KEY, CELONIS_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedValue.toString()));
    }
}
