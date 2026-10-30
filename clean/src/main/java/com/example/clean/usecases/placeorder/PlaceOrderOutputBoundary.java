package com.example.clean.usecases.placeorder;

import java.math.BigDecimal;

public interface PlaceOrderOutputBoundary {

    void present(Response response);

    record Response(String id, BigDecimal total) {
    }
}
