package com.example.tasks.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class TaskReadDto {

    long id;
    String title;
    String description;
    LocalDate dueDate;
    boolean completed;
}
