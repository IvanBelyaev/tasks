package com.example.tasks.controller;

import com.example.tasks.dto.PageResponse;
import com.example.tasks.dto.TaskCreateEditDto;
import com.example.tasks.dto.TaskReadDto;
import com.example.tasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public PageResponse<TaskReadDto> findAll(
            @ParameterObject @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        var allTasks = taskService.findAll(pageable);
        return PageResponse.of(allTasks);
    }

    @GetMapping("/{id}")
    public TaskReadDto findById(@PathVariable("id") Long id) {
        return taskService.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "task with id = " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskReadDto create(@Validated @RequestBody TaskCreateEditDto taskDto) {
        return taskService.create(taskDto);
    }

    @PutMapping("/{id}")
    public TaskReadDto update(@PathVariable("id") Long id,
            @Validated @RequestBody TaskCreateEditDto taskDto) {
        return taskService.update(id, taskDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (!taskService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
