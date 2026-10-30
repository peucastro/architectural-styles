package com.example.clean.usecases.registeruser;

/** Input boundary: the entry point the controller calls. */
public interface RegisterUserInputBoundary {

    void execute(Request request);

    record Request(String name, String email) {
    }
}
