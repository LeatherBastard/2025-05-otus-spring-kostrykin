package ru.otus.hw.services.validation;

import ru.otus.hw.model.Order;

public interface OrderValidationService {
    Order validateOrder(Order order);
}
