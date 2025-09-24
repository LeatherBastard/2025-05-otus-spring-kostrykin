package ru.otus.hw.services.supplement;

import org.springframework.stereotype.Service;
import ru.otus.hw.model.Order;

import java.time.LocalDateTime;

@Service
public class OrderSupplementServiceImpl implements OrderSupplementService {

    @Override
    public Order supplyOrder(Order order) {
        order.setCreationDate(LocalDateTime.now());
        return order;
    }


}
