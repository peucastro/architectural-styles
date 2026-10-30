package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.RegisterUserUseCase;
import com.example.hexagonal.application.port.out.UserRepository;
import com.example.hexagonal.domain.User;

public class UserService implements RegisterUserUseCase {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    @Override
    public User register(String name, String email) {
        if (users.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("email already registered");
        }
        User user = User.register(name, email);
        users.save(user);
        return user;
    }
}
