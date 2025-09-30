package ru.otus.hw.services.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;

import java.util.Collection;

@MessagingGateway
public interface ItemGateway {
    @Gateway(requestChannel = "itemsChannel", replyChannel = "readyOrderChannel")
    Order process(Collection<Item> items);
}
