package ru.otus.hw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;
import ru.otus.hw.services.gateway.ItemGateway;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SpringIntegrationTest {
    @Autowired
    private ItemGateway itemGateway;

    @Test
    void shouldProcessCorrect() {
        List<Item> items = List.of(
                new Item(1L, "Item1", 2L),
                new Item(2L, "Item2", 3L),
                new Item(3L, "Item3", 2L)
        );
        Order order = itemGateway.process(items);
        assertThat(order).isNotNull();
        assertThat(order.getId()).isNotNull();
        assertThat(order.getName()).isNotNull();
        assertThat(order.getItems().size()).isEqualTo(2);
        assertThat(order.isValid()).isEqualTo(true);
        assertThat(order.getCreationDate()).isNotNull();
    }

}
