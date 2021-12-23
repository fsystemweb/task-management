package com.celonis.challenge.model.validation;

import com.celonis.challenge.model.EnumTypeTask;
import com.celonis.challenge.model.ProjectGenerationTask;

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
