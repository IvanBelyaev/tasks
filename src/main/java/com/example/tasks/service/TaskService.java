package com.example.tasks.service;

import com.example.tasks.dto.TaskCreateEditDto;
import com.example.tasks.dto.TaskReadDto;
import com.example.tasks.maper.TaskCreateMapper;
import com.example.tasks.maper.TaskReadMapper;
import com.example.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskReadMapper taskReadMapper;

    private final TaskCreateMapper taskCreateMapper;

    public Page<TaskReadDto> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskReadMapper::map);
    }

    public Optional<TaskReadDto> findById(Long id) {
        return taskRepository.findById(id)
                .flatMap(task -> Optional.of(taskReadMapper.map(task)));
    }

    @Transactional
    public TaskReadDto create(TaskCreateEditDto taskDto) {
        return Optional.of(taskDto)
                .map(taskCreateMapper::map)
                .map(taskRepository::save)
                .map(taskReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<TaskReadDto> update(Long id, TaskCreateEditDto taskDto) {
        return taskRepository.findById(id)
                .map(entity -> taskCreateMapper.map(taskDto, entity))
                .map(taskRepository::saveAndFlush)
                .map(taskReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return taskRepository.findById(id)
                .map(user -> {
                    taskRepository.delete(user);
                    taskRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}

