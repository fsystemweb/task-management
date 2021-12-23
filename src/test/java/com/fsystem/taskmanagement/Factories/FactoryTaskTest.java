package com.fsystem.taskmanagement.Factories;

import com.fsystem.taskmanagement.model.EnumTypeTask;
import com.fsystem.taskmanagement.model.ProjectGenerationTask;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public  class FactoryTaskTest {
    public static ProjectGenerationTask generateTask(){
        return createTask("MyTask1" );
    }
    public static ProjectGenerationTask generateCountTask(){
        ProjectGenerationTask task =  createTask("MyTask1" );
        task.setType(EnumTypeTask.COUNT_TASK);
        task.setStart(0);
        task.setEnd(2);

        return task;
    }

    public static List<ProjectGenerationTask> generateTaskList(){
        ProjectGenerationTask task1 = createTask("MyTask1" );
        ProjectGenerationTask task2 = createTask("MyTask2" );

        List<ProjectGenerationTask> taskList = new ArrayList<>();
        taskList.add(task1);
        taskList.add(task2);

        return taskList;
    }


    private static ProjectGenerationTask createTask(String name){
        ProjectGenerationTask projectGenerationTask = new ProjectGenerationTask();

        Date input = new Date();
        Instant instant = input.toInstant();
        Date output = Date.from(instant);

        projectGenerationTask.setCreationDate(output);
        projectGenerationTask.setId(name);
        projectGenerationTask.setStorageLocation("myDirectory/file.zip");
        projectGenerationTask.setName(name);
        projectGenerationTask.setType(EnumTypeTask.GENERATE_SAMPLE_PROJECT);

        return projectGenerationTask;
    }
}
