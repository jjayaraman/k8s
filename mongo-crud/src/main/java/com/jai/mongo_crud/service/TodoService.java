package com.jai.mongo_crud.service;

import com.jai.mongo_crud.data.TodoRepository;
import com.jai.mongo_crud.model.Todo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> listTodos() {
        return todoRepository.findAll();
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Optional<Todo> findById(String id) {
        return todoRepository.findById(id);
    }

    public void deleteById(String id) {
        todoRepository.deleteById(id);
    }

}
