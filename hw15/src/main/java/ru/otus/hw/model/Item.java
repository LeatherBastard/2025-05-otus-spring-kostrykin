package ru.otus.hw.model;

import lombok.Data;

@Data
public class Item {
    private final Long id;
    private final String name;
    private final Long orderId;
}
