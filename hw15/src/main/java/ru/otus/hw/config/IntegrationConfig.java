package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.model.Item;
import ru.otus.hw.model.Order;
import ru.otus.hw.services.fulfillment.OrderFulfillmentService;
import ru.otus.hw.services.supplement.OrderSupplementService;
import ru.otus.hw.services.validation.OrderValidationService;

@Configuration
public class IntegrationConfig {
    private static final long ORDER_ID = 2L;

    @Bean
    MessageChannelSpec<?, ?> itemsChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public IntegrationFlow orderItemsFlow() {
        return IntegrationFlow.from(itemsChannel())
                .<Item>filter(item -> item.getOrderId() == ORDER_ID)
                .channel("orderChannel")
                .get();

    }

    @Bean
    public IntegrationFlow orderValidationFlow(OrderValidationService validationService) {
        return IntegrationFlow.from("orderChannel")
                .split()
                .transform(order -> validationService.validateOrder((Order) order))
                .<Order, Boolean>route(Order::isValid, mapping -> mapping
                        .subFlowMapping(true, sf -> sf.channel("supplementOrderChannel"))
                        .subFlowMapping(false, sf -> sf.transform(order -> {
                                    throw new RuntimeException("Invalid Order");
                                })
                                .aggregate())
                ).get();

    }

    @Bean
    public IntegrationFlow orderSupplementFlow(OrderSupplementService supplementService) {
        return IntegrationFlow.from("supplementOrderChannel")
                .handle(supplementService, "supplyOrder")
                .channel("fulfillmentOrderChannel")
                .get();
    }

    @Bean
    public IntegrationFlow orderFulfillmentFlow(OrderFulfillmentService fulfillmentService) {
        return IntegrationFlow.from("fulfillmentOrderChannel")
                .handle(fulfillmentService, "fulfillOrder")
                .get();
    }

}
