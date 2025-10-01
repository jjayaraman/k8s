package com.jai.mongo_crud.controller;

import com.jai.mongo_crud.model.Todo;
import com.jai.mongo_crud.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/list")
    public List<Todo> listTodos() {
        return todoService.listTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> findById(@PathVariable String id) {
        return todoService.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        Todo created = todoService.create(todo);
        URI location = URI.create("/api/todo/" + created.id());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        todoService.deleteById(id);
    }
}
