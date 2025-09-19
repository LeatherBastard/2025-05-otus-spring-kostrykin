package ru.otus.hw.services.fulfillment;

import ru.otus.hw.model.Order;

public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {
    @Override
    public String fulfillOrder(Order order) {
        return String.format("Order with id %d is fulfilled", order.getId());
    }
}
