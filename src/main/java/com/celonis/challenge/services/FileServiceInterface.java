package com.celonis.challenge.services;

import com.celonis.challenge.model.ProjectGenerationTask;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.net.URL;

public interface FileServiceInterface {
    FileSystemResource getTaskResult(ProjectGenerationTask projectGenerationTask);

    void storeResult(ProjectGenerationTask projectGenerationTask, URL url) throws IOException;
}
