package com.jai.mongo_crud.service;

import com.jai.mongo_crud.data.TodoRepository;
import com.jai.mongo_crud.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo testTodo;
    private Todo testTodo2;

    @BeforeEach
    void setUp() {
        testTodo = new Todo("1", 100L, "Test Todo 1", false);
        testTodo2 = new Todo("2", 100L, "Test Todo 2", true);
    }

    @Test
    void testListTodos() {
        // Given
        List<Todo> todos = Arrays.asList(testTodo, testTodo2);
        when(todoRepository.findAll()).thenReturn(todos);

        // When
        List<Todo> result = todoService.listTodos();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testTodo, result.get(0));
        assertEquals(testTodo2, result.get(1));
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testListTodosWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        @SuppressWarnings("null")
        Page<Todo> todoPage = new PageImpl<>(Arrays.asList(testTodo, testTodo2));
        when(todoRepository.findAll(pageable)).thenReturn(todoPage);

        // When
        Page<Todo> result = todoService.listTodos(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        verify(todoRepository, times(1)).findAll(pageable);
    }

    @Test
    void testCreate() {
        // Given
        Todo newTodo = new Todo(null, 200L, "New Todo", false);
        Todo savedTodo = new Todo("3", 200L, "New Todo", false);
        when(todoRepository.save(newTodo)).thenReturn(savedTodo);

        // When
        Todo result = todoService.create(newTodo);

        // Then
        assertNotNull(result);
        assertEquals("3", result.id());
        assertEquals("New Todo", result.title());
        verify(todoRepository, times(1)).save(newTodo);
    }

    @Test
    void testFindById_Found() {
        // Given
        String id = "1";
        when(todoRepository.findById(id)).thenReturn(Optional.of(testTodo));

        // When
        Optional<Todo> result = todoService.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testTodo, result.get());
        verify(todoRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // Given
        String id = "999";
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Todo> result = todoService.findById(id);

        // Then
        assertFalse(result.isPresent());
        verify(todoRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById_Success() {
        // Given
        String id = "1";
        when(todoRepository.existsById(id)).thenReturn(true);
        doNothing().when(todoRepository).deleteById(id);

        // When
        todoService.deleteById(id);

        // Then
        verify(todoRepository, times(1)).existsById(id);
        verify(todoRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteById_NotFound() {
        // Given
        String id = "999";
        when(todoRepository.existsById(id)).thenReturn(false);

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            todoService.deleteById(id);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(todoRepository, times(1)).existsById(id);
        verify(todoRepository, never()).deleteById(id);
    }

    @Test
    @SuppressWarnings("null")
    void testUpdate_Success() {
        // Given
        String id = "1";
        Todo updatedTodoData = new Todo(null, 100L, "Updated Todo", true);
        Todo existingTodo = new Todo("1", 100L, "Old Todo", false);
        Todo savedTodo = new Todo("1", 100L, "Updated Todo", true);

        when(todoRepository.findById(id)).thenReturn(Optional.of(existingTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // When
        Todo result = todoService.update(id, updatedTodoData);

        // Then
        assertNotNull(result);
        assertEquals("1", result.id()); // ID should remain the same
        assertEquals("Updated Todo", result.title());
        assertTrue(result.completed());
        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    @SuppressWarnings("null")
    void testUpdate_NotFound() {
        // Given
        String id = "999";
        Todo updatedTodoData = new Todo(null, 100L, "Updated Todo", true);
        when(todoRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            todoService.update(id, updatedTodoData);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(todoRepository, times(1)).findById(id);
        verify(todoRepository, never()).save(any(Todo.class));
    }
}
