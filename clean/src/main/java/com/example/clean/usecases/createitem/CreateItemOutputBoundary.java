package com.example.clean.usecases.createitem;

public interface CreateItemOutputBoundary {

    void present(Response response);

    record Response(String id) {
    }
}
