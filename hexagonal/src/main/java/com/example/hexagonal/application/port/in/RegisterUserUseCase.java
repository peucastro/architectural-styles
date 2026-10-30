package com.example.hexagonal.application.port.in;

import com.example.hexagonal.domain.User;

public interface RegisterUserUseCase {
    User register(String name, String email);
}
