package ru.otus.hw.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private final Long id;

    private final String name;

    private boolean valid;

    private LocalDateTime creationDate;

    private final List<Item> items;
}
