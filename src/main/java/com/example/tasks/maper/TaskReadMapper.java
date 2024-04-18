package com.example.tasks.maper;

import com.example.tasks.dto.TaskReadDto;
import com.example.tasks.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskReadMapper implements Mapper<Task, TaskReadDto> {

    @Override
    public TaskReadDto map(Task object) {
        return TaskReadDto.builder()
                .id(object.getId())
                .title(object.getTitle())
                .description(object.getDescription())
                .dueDate(object.getDueDate())
                .completed(object.isCompleted())
                .build();
    }
}
