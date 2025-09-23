package ru.otus.hw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;
import ru.otus.hw.services.creation.OrderCreationService;
import ru.otus.hw.services.fulfillment.OrderFulfillmentService;
import ru.otus.hw.services.supplement.OrderSupplementService;
import ru.otus.hw.services.validation.OrderValidationService;

import java.util.List;

@Configuration
@Slf4j
public class IntegrationConfig {
    private static final Long ORDER_ID = 2L;

    @Bean
    MessageChannelSpec<?, ?> itemsChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean
    MessageChannelSpec<?, ?> readyOrderChannel() {
        return MessageChannels.queue(10);
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow orderItemsFlow(OrderCreationService orderCreationService) {
        return IntegrationFlow.from(itemsChannel())
                .split()
                .<Item>filter(item -> ORDER_ID.equals(item.getOrderId()))
                .aggregate(agregator -> agregator
                        .correlationStrategy(message -> "filteredOrder")
                        .releaseStrategy(group -> group.size() >= 1)

                )
                .<List<Item>, Order>transform(items -> orderCreationService.createOrder(items))
                .log()
                .channel("orderChannel")
                .get();
    }

    @Bean
    public IntegrationFlow orderValidationFlow(OrderValidationService validationService) {
        return IntegrationFlow.from("orderChannel")
                .transform(Order.class, order -> validationService.validateOrder(order))
                .<Order, Boolean>route(Order::isValid, mapping -> mapping
                        .subFlowMapping(true, sf -> sf.channel("supplementOrderChannel"))
                        .subFlowMapping(false, sf -> sf.transform(order -> {
                            throw new RuntimeException("Invalid Order");
                        }))
                )
                .get();

    }

    @Bean
    public IntegrationFlow orderSupplementFlow(OrderSupplementService supplementService) {
        return IntegrationFlow.from("supplementOrderChannel")
                .handle(supplementService, "supplyOrder")
                .channel("readyOrderChannel")
                .get();
    }


    @Bean
    public IntegrationFlow orderFulfillmentFlow(OrderFulfillmentService fulfillmentService) {
        return IntegrationFlow.from("fulfillmentOrderChannel")
                .handle(fulfillmentService, "fulfillOrder")
                .get();
    }

}
