package com.example.clean.usecases.registeruser;

import com.example.clean.entities.User;
import com.example.clean.usecases.gateways.UserGateway;

public class RegisterUserInteractor implements RegisterUserInputBoundary {

    private final UserGateway users;
    private final RegisterUserOutputBoundary output;

    public RegisterUserInteractor(UserGateway users, RegisterUserOutputBoundary output) {
        this.users = users;
        this.output = output;
    }

    @Override
    public void execute(Request request) {
        if (users.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("email already registered");
        }
        User user = User.register(request.name(), request.email());
        users.save(user);
        output.present(new RegisterUserOutputBoundary.Response(user.id()));
    }
}
