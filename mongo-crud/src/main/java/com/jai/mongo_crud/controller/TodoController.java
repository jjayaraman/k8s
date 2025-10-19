package com.jai.mongo_crud.controller;

import com.jai.mongo_crud.model.Todo;
import com.jai.mongo_crud.service.TodoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private static final Logger log = LoggerFactory.getLogger(TodoController.class);
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping()
    public Page<Todo> listTodos(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Returning paginated records for page: {} and size: {}", page, size);
        return todoService.listTodos(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findById(@PathVariable String id) {
        return todoService.findById(id).map(ResponseEntity::ok).orElseThrow(() -> {
            log.warn("No data found for the given id: {}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
    }

    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo) {
        Todo created = todoService.create(todo);
        URI location = URI.create("/api/todos/" + created.id());
        log.info("Record created: {}", todo.toString());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable String id, @Valid @RequestBody Todo todo) {
        Todo updated = todoService.update(id, todo);
        log.info("Record updated: {}", todo.toString());
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        todoService.deleteById(id);
        log.info("Record deleted for id: {}", id);
    }
}
