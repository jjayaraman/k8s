package com.jai.mongo_crud.data;

import com.jai.mongo_crud.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TodoRepository extends MongoRepository<Todo,Long> {
}
