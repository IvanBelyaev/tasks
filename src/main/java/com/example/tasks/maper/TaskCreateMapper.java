package com.example.tasks.maper;

import com.example.tasks.dto.TaskCreateEditDto;
import com.example.tasks.entity.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class TaskCreateMapper implements Mapper<TaskCreateEditDto, Task> {

    @Override
    public Task map(TaskCreateEditDto object) {
        var task = new Task();
        copy(object, task);
        return task;
    }

    @Override
    public Task map(TaskCreateEditDto fromObject, Task toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    private void copy(TaskCreateEditDto taskDto, Task task) {
        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setDueDate(Optional.ofNullable(taskDto.dueDate()).orElse(LocalDate.now()));
        task.setCompleted(Optional.ofNullable(taskDto.completed()).orElse(false));
    }
}
