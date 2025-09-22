package ru.otus.hw.services.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;
import ru.otus.hw.services.gateway.ItemGateway;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemGeneratorServiceImpl implements ItemGeneratorService {


    private final ItemGateway itemGateway;

    @Override
    public void startGenerateItems() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 10; i++) {
            int num = i + 1;
            pool.execute(() -> {
                Collection<Item> items = generateItems();
                log.info("{}, New items: {}", num,
                        items.stream().map(Item::getName)
                                .collect(Collectors.joining(",")));

                Order order = itemGateway.process(items);
                log.info("{}, Ready order of items: {}", num, order.getItems().stream()
                        .map(Item::getName)
                        .collect(Collectors.joining(",")));
            });

            delay();
        }

    }

    private static Collection<Item> generateItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            items.add(new Item((long) i, "Item" + i, RandomUtils.nextLong(1, 3)));
        }
        return items;
    }

    private void delay() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
