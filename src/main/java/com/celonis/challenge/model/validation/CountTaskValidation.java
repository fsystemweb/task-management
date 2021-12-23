package com.celonis.challenge.model.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CountTaskValidator.class)
public @interface CountTaskValidation {
    public String message() default "Invalid Range: must be an integer, start must be minor that end," +
            " start must be mayor or equal to 0";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
