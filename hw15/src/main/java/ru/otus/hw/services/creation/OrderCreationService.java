package ru.otus.hw.services.creation;

import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;

import java.util.List;

public interface OrderCreationService {
    Order createOrder(List<Item> items);
}
