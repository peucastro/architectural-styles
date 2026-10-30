package com.example.clean.entities;

import java.util.UUID;

public record User(String id, String name, String email) {

    public static User register(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("email is invalid");
        }
        return new User(UUID.randomUUID().toString(), name, email);
    }
}
