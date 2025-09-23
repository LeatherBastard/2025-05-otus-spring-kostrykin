package ru.otus.hw.services.supplement;

import org.springframework.stereotype.Service;
import ru.otus.hw.model.Order;

import java.time.LocalDateTime;

@Service
public class OrderSupplementServiceImpl implements OrderSupplementService {

    @Override
    public Order supplyOrder(Order order) {
        order.setCreationDate(LocalDateTime.now());
      //  delay();
        return order;
    }

    private static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
