package com.jai.mongo_crud.controller;

import com.jai.mongo_crud.model.Todo;
import com.jai.mongo_crud.service.TodoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
