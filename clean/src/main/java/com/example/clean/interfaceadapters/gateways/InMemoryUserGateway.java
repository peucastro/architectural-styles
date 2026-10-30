package com.example.clean.interfaceadapters.gateways;

import com.example.clean.entities.User;
import com.example.clean.usecases.gateways.UserGateway;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserGateway implements UserGateway {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        users.put(user.id(), user);
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream().filter(u -> u.email().equals(email)).findFirst();
    }
}
