package com.example.clean.usecases.registeruser;

/** Output boundary: the interactor hands its result here; the presenter implements it. */
public interface RegisterUserOutputBoundary {

    void present(Response response);

    record Response(String id) {
    }
}
