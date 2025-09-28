package com.jai.mongo_crud.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "todos")
public record Todo(String id, Long userId, String title, boolean completed) {
}
