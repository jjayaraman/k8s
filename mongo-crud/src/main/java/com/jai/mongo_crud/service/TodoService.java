package com.jai.mongo_crud.service;

import com.jai.mongo_crud.data.TodoRepository;
import com.jai.mongo_crud.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private static final Logger log = LoggerFactory.getLogger(TodoService.class);

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> listTodos() {
        return todoRepository.findAll();
    }

    public Page<Todo> listTodos(Pageable pageable) {
        return todoRepository.findAll(pageable);
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Optional<Todo> findById(String id) {
        return todoRepository.findById(id);
    }

    public void deleteById(String id) {
        if (!todoRepository.existsById(id)) {
            log.error("No record found for the given id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        todoRepository.deleteById(id);
    }

    public Todo update(String id, Todo todo) {
        return todoRepository.findById(id).map(existingTodo -> {
            Todo updatedTodo = new Todo(
                    id, // keep the same id
                    todo.userId(),
                    todo.title(),
                    todo.completed());
            return todoRepository.save(updatedTodo);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
           }

}
