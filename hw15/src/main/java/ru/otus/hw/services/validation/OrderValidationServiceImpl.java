package ru.otus.hw.services.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.Transformer;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Order;

@Service
@Slf4j
public class OrderValidationServiceImpl implements OrderValidationService {
    @Override
    @Transformer
    public Order validateOrder(Order order) {
        boolean isIdEmpty = order.getId() == null;

        boolean isItemsEmpty = order.getItems().isEmpty();
        if (isIdEmpty || isItemsEmpty) {
            order.setValid(false);
        }
        order.setValid(true);
        return order;
    }


}
