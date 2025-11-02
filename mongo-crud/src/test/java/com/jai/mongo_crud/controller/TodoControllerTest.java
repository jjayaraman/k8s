package com.jai.mongo_crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jai.mongo_crud.model.Todo;
import com.jai.mongo_crud.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Todo testTodo;
    private Todo testTodo2;

    @BeforeEach
    void setUp() {
        testTodo = new Todo("1", 100L, "Test Todo 1", false);
        testTodo2 = new Todo("2", 100L, "Test Todo 2", true);
    }

    @Test
    void testListTodos_DefaultPagination() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Todo> todoPage = new PageImpl<>(Arrays.asList(testTodo, testTodo2));
        when(todoService.listTodos(any(Pageable.class))).thenReturn(todoPage);

        // When & Then
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].title").value("Test Todo 1"))
                .andExpect(jsonPath("$.content[1].id").value("2"));

        verify(todoService, times(1)).listTodos(any(Pageable.class));
    }

    @Test
    void testListTodos_CustomPagination() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(1, 5);
        Page<Todo> todoPage = new PageImpl<>(Arrays.asList(testTodo2));
        when(todoService.listTodos(any(Pageable.class))).thenReturn(todoPage);

        // When & Then
        mockMvc.perform(get("/api/todos")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));

        verify(todoService, times(1)).listTodos(pageable);
    }

    @Test
    void testFindById_Success() throws Exception {
        // Given
        String id = "1";
        when(todoService.findById(id)).thenReturn(Optional.of(testTodo));

        // When & Then
        mockMvc.perform(get("/api/todos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Test Todo 1"))
                .andExpect(jsonPath("$.userId").value(100))
                .andExpect(jsonPath("$.completed").value(false));

        verify(todoService, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() throws Exception {
        // Given
        String id = "999";
        when(todoService.findById(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/todos/{id}", id))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).findById(id);
    }

    @Test
    void testCreate_Success() throws Exception {
        // Given
        Todo newTodo = new Todo(null, 200L, "New Todo", false);
        Todo createdTodo = new Todo("3", 200L, "New Todo", false);
        when(todoService.create(any(Todo.class))).thenReturn(createdTodo);

        // When & Then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTodo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.title").value("New Todo"))
                .andExpect(header().string("Location", "/api/todos/3"));

        verify(todoService, times(1)).create(any(Todo.class));
    }

    @Test
    void testCreate_ValidationFailure() throws Exception {
        // Given - Todo with blank title (should fail validation)
        Todo invalidTodo = new Todo(null, 200L, "", false);

        // When & Then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTodo)))
                .andExpect(status().isBadRequest());

        verify(todoService, never()).create(any(Todo.class));
    }

    @Test
    void testUpdate_Success() throws Exception {
        // Given
        String id = "1";
        Todo updateData = new Todo(null, 100L, "Updated Todo", true);
        Todo updatedTodo = new Todo("1", 100L, "Updated Todo", true);
        when(todoService.update(eq(id), any(Todo.class))).thenReturn(updatedTodo);

        // When & Then
        mockMvc.perform(put("/api/todos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Updated Todo"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(todoService, times(1)).update(eq(id), any(Todo.class));
    }

    @Test
    void testUpdate_ValidationFailure() throws Exception {
        // Given
        String id = "1";
        Todo invalidTodo = new Todo(null, 100L, "", true);

        // When & Then
        mockMvc.perform(put("/api/todos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTodo)))
                .andExpect(status().isBadRequest());

        verify(todoService, never()).update(anyString(), any(Todo.class));
    }

    @Test
    void testDelete_Success() throws Exception {
        // Given
        String id = "1";
        doNothing().when(todoService).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/todos/{id}", id))
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).deleteById(id);
    }
}
