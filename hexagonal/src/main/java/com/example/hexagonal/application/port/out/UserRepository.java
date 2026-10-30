package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);
}
