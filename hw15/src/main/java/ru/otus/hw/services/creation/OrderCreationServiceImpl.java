package ru.otus.hw.services.creation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderCreationServiceImpl implements OrderCreationService {
    @Override
    public Order createOrder(List<Item> items) {
        log.info("Creating Order...");
        Order order = new Order(items.get(0).getOrderId(), "Order", new ArrayList<>());
        items.stream().forEach(item -> order.getItems().add(item));
        log.info(" Order with items: {}", order.getItems().stream()
                .map(Item::getName)
                .collect(Collectors.joining(",")));
        return order;
    }
}
