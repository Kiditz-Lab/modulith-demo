package com.example.demo.inventories;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryAmqpConfiguration {
    public static final String INVENTORY_Q = "inventories";
    public static final String INVENTORY_FAILED_Q = "inventory_failed";

    @Bean
    FanoutExchange inventoryExchange() {
        return ExchangeBuilder.fanoutExchange(INVENTORY_FAILED_Q).build();
    }

    @Bean
    Queue inventoryQueue() {
        return QueueBuilder.durable(INVENTORY_Q).build();
    }

    @Bean
    Binding inventoryBinding(FanoutExchange inventoryExchange, Queue inventoryQueue) {
        return BindingBuilder.bind(inventoryQueue).to(inventoryExchange);
    }

    @Bean
    FanoutExchange inventoryFailedExchange() {
        return ExchangeBuilder.fanoutExchange(INVENTORY_FAILED_Q).build();
    }

    @Bean
    Queue inventoryFailedQueue() {
        return QueueBuilder.durable(INVENTORY_FAILED_Q).build();
    }

    @Bean
    Binding inventoryFailedBinding(FanoutExchange inventoryFailedExchange, Queue inventoryFailedQueue) {
        return BindingBuilder.bind(inventoryFailedQueue).to(inventoryFailedExchange);
    }
}
