package ru.otus.hw.services.fulfillment;

import ru.otus.hw.model.Order;

public interface OrderFulfillmentService {
    String fulfillOrder(Order order);
}
