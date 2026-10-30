package com.example.hexagonal.application.port.in;

import com.example.hexagonal.domain.Item;

import java.math.BigDecimal;

public interface CreateItemUseCase {
    Item create(String name, BigDecimal price);
}
