package com.example.onion.application.repository;

import com.example.onion.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);
}
