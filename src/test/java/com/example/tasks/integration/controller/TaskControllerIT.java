package com.example.tasks.integration.controller;

import com.example.tasks.dto.TaskCreateEditDto;
import com.example.tasks.integration.IntegrationTestBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class TaskControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.content[*].title")
                        .value(containsInAnyOrder("task_1", "task_2", "task_3")))
                .andExpect(jsonPath("$.metadata.totalElements").value(equalTo(3)));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.title").value(equalTo("task_1")));
    }

    @Test
    void findById_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/4"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void create() throws Exception {
        String json = asJsonString(
                        new TaskCreateEditDto("task_4", "description_4", null, null));
        mockMvc.perform(post("/tasks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(equalTo("task_4")))
                .andExpect(jsonPath("$.description").value("description_4"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void create_whenNoTitle() throws Exception {
        String json = asJsonString(
                new TaskCreateEditDto(null, null, null, null));
        mockMvc.perform(post("/tasks")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").value(contains("title must not be empty")));
    }

    @Test
    void update() throws Exception {
        String json = asJsonString(
                new TaskCreateEditDto("new title", null, null, null));
        mockMvc.perform(put("/tasks/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.title").value("new title"));
    }

    @Test
    void update_whenTaskNotFound() throws Exception {
        String json = asJsonString(
                new TaskCreateEditDto("new title", null, null, null));
        mockMvc.perform(put("/tasks/4")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_whenTaskNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/4"))
                .andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
