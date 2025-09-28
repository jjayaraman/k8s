package com.jai.mongo_crud.controller;

import com.jai.mongo_crud.model.Todo;
import com.jai.mongo_crud.service.TodoService;
import org.springframework.web.bind.annotation.*;

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
    public Todo findById(@PathVariable String id) {
        return todoService.findById(id);
    }

    @PostMapping("/create")
    public Todo create(@RequestBody Todo todo) {
        return todoService.create(todo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        todoService.deleteById(id);
    }
}
