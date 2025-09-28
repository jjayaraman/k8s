package com.jai.mongo_crud.service;

import com.jai.mongo_crud.data.TodoRepository;
import com.jai.mongo_crud.model.Todo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> listTodos() {
        return todoRepository.findAll();
    }
}
