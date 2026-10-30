package com.example.onion.application.service;

import com.example.onion.application.repository.UserRepository;
import com.example.onion.domain.model.User;

public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public User register(String name, String email) {
        if (users.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("email already registered");
        }
        User user = User.register(name, email);
        users.save(user);
        return user;
    }
}
