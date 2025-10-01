package com.jai.mongo_crud.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "todos")
public record Todo(@Id String id, @NotNull Long userId, @NotBlank String title, boolean completed) {
}
