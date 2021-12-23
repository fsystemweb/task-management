package com.fsystem.taskmanagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BadRequestError {
    private String message;
}
