package com.fsystem.taskmanagement.model.validation;

import com.fsystem.taskmanagement.model.EnumTypeTask;
import com.fsystem.taskmanagement.model.ProjectGenerationTask;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountTaskValidator implements ConstraintValidator<CountTaskValidation, ProjectGenerationTask> {
    public void initialize(CountTaskValidation constraintAnnotation) {

    }

    @Override
    public boolean isValid(ProjectGenerationTask projectGenerationTask,
                           ConstraintValidatorContext constraintValidatorContext) {
        if(projectGenerationTask.getType() != EnumTypeTask.COUNT_TASK)
            return true;

        if(projectGenerationTask.getStart() < 0)
            return false;

        if(projectGenerationTask.getStart() >= projectGenerationTask.getEnd())
            return false;

        return true;
    }
}
