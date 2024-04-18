package com.example.tasks.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TaskCreateEditDto(@NotBlank(message = "title must not be empty") String title,
                                String description,
                                LocalDate dueDate,
                                Boolean completed) {
}
