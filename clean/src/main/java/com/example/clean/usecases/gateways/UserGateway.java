package com.example.clean.usecases.gateways;

import com.example.clean.entities.User;

import java.util.Optional;

/** Gateway interface owned by the use case layer; implemented in interface adapters. */
public interface UserGateway {
    void save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);
}
