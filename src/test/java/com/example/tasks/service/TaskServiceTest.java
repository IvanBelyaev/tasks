package com.example.tasks.service;

import com.example.tasks.dto.TaskCreateEditDto;
import com.example.tasks.entity.Task;
import com.example.tasks.maper.TaskCreateMapper;
import com.example.tasks.maper.TaskReadMapper;
import com.example.tasks.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class TaskServiceTest {

    @Spy
    TaskReadMapper taskReadMapper = new TaskReadMapper();

    @Spy
    TaskCreateMapper taskCreateMapper = new TaskCreateMapper();

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    private final Task someTask =
            new Task(1L, "task_1", "some text",
                    LocalDate.of(2020, 10, 5), false);

    @Test
    void findAll() {
        when(taskRepository.findAll((any(Pageable.class)))).thenReturn(new PageImpl<>(List.of(someTask)));

        var allTasks = taskService.findAll(Pageable.unpaged());

        assertThat(allTasks.getSize()).isEqualTo(1);
        assertThat(allTasks.getTotalElements()).isEqualTo(1);
        assertThat(allTasks.getContent().get(0).getTitle()).isEqualTo("task_1");
    }

    @Test
    void findById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(someTask));

        var maybeTask = taskService.findById(1L);

        assertThat(maybeTask).isPresent();
        maybeTask.ifPresent(taskDto -> assertThat(taskDto.getTitle()).isEqualTo("task_1"));
    }

    @Test
    void fidById_whenTaskDoesNotExist() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        var maybeTask = taskService.findById(2L);

        assertThat(maybeTask).isNotPresent();
    }

    @Test
    void create() {
        var taskCreateEditDto = new TaskCreateEditDto("new task", "new description",
                LocalDate.of(2010, 8, 3), true);
        var newTask = taskCreateMapper.map(taskCreateEditDto);
        newTask.setId(2L);
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        var taskReadDto = taskService.create(taskCreateEditDto);

        assertThat(taskReadDto.getId()).isEqualTo(2L);
        assertThat(taskReadDto.getTitle()).isEqualTo("new task");
        assertThat(taskReadDto.getDescription()).isEqualTo("new description");
        assertThat(taskReadDto.getDueDate()).isEqualTo(LocalDate.of(2010, 8, 3));
        assertThat(taskReadDto.isCompleted()).isTrue();
    }

    @Test
    void update() {
        var taskCreateEditDto = new TaskCreateEditDto("new task", "new description",
                LocalDate.of(2010, 8, 3), true);
        var newTask = taskCreateMapper.map(taskCreateEditDto);
        newTask.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(someTask));
        lenient().when(taskRepository.saveAndFlush(newTask)).thenReturn(newTask);

        var maybeTaskReadDto = taskService.update(1L, taskCreateEditDto);

        assertThat(maybeTaskReadDto).isPresent();
        var taskReadDto = maybeTaskReadDto.get();
        assertThat(taskReadDto.getTitle()).isEqualTo("new task");
        assertThat(taskReadDto.getDescription()).isEqualTo("new description");
        assertThat(taskReadDto.getDueDate()).isEqualTo(LocalDate.of(2010, 8, 3));
        assertThat(taskReadDto.isCompleted()).isTrue();
    }

    @Test
    void update_whenTaskDoesNotExist() {
        var taskCreateEditDto = new TaskCreateEditDto("new task", "new description",
                LocalDate.of(2010, 8, 3), true);
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        var maybeTaskReadDto = taskService.update(2L, taskCreateEditDto);

        assertThat(maybeTaskReadDto).isNotPresent();
    }

    @Test
    void delete() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(someTask));
        doNothing().when(taskRepository).delete(someTask);
        doNothing().when(taskRepository).flush();

        var result = taskService.delete(1L);

        assertThat(result).isTrue();
    }

    @Test
    void delete_whenTaskDoesNotExist() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        var result = taskService.delete(2L);

        assertThat(result).isFalse();
    }
}
