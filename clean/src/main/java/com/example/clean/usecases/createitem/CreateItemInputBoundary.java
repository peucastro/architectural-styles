package com.example.clean.usecases.createitem;

import java.math.BigDecimal;

public interface CreateItemInputBoundary {

    void execute(Request request);

    record Request(String name, BigDecimal price) {
    }
}
