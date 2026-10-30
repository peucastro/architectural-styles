package com.example.clean.usecases.placeorder;

import java.util.List;

public interface PlaceOrderInputBoundary {

    void execute(Request request);

    record Request(String userId, List<String> itemIds) {
    }
}
