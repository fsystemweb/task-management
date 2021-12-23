package com.celonis.challenge.services;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.model.ProjectGenerationTaskRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.celonis.challenge.Factories.FactoryTaskTest.generateTask;
import static com.celonis.challenge.constants.Contants.PROJECT_FILENAME;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    private FileService fileService;

    @Mock
    ProjectGenerationTaskRepository projectGenerationTaskRepository;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        fileService = new FileService(projectGenerationTaskRepository);
    }

    @Test
    void testGetTaskResult_FailFileNotGenerated() {
        try {
            fileService.getTaskResult(generateTask());
            Assert.fail("Expected exception to be thrown");
        } catch (InternalException e) {
            assertThat(e)
                    .isInstanceOf(InternalException.class)
                    .hasMessage("File not generated yet");
        }
    }

    @Test
    void testGetTaskResult_OK() {
        ProjectGenerationTask projectGenerationTask = generateTask();

        projectGenerationTask.setStorageLocation("src/test/resources/"+PROJECT_FILENAME);

        FileSystemResource result = fileService.getTaskResult(projectGenerationTask);

        assertTrue(result.toString().contains(PROJECT_FILENAME));
    }

    @Test
    void testStoreResult_OK() throws IOException {
        fileService.storeResult(generateTask(), new File("src/test/resources/"+PROJECT_FILENAME).toURI().toURL());
        verify(projectGenerationTaskRepository, times(1)).save(any());
    }

    @Test
    void testStoreResult_FailInvalidPath() throws IOException {
        try {
            fileService.storeResult(generateTask(), new File("**->,<.zip").toURI().toURL());
            verify(projectGenerationTaskRepository, times(0)).save(any());
            Assert.fail("Expected exception to be thrown");
        } catch (FileNotFoundException e) {
            assertThat(e)
                    .isInstanceOf(FileNotFoundException.class);
        }
    }
}
