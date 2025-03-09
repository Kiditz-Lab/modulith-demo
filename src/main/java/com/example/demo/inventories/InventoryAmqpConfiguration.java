package com.example.demo.inventories;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryAmqpConfiguration {
    public static final String INVENTORY_Q = "inventories";
    public static final String INVENTORY_FAILED_Q = "inventory_failed";

    @Bean
    Binding inventoryBinding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(INVENTORY_Q).noargs();
    }

    @Bean
    Exchange inventoryExchange() {
        return ExchangeBuilder.directExchange(INVENTORY_Q).build();
    }

    @Bean
    Queue inventoryQueue() {
        return QueueBuilder.durable(INVENTORY_Q).build();
    }

    @Bean
    Binding inventoryFailedBinding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(INVENTORY_FAILED_Q).noargs();
    }

    @Bean
    Exchange inventoryFailedExchange() {
        return ExchangeBuilder.directExchange(INVENTORY_FAILED_Q).build();
    }

    @Bean
    Queue inventoryFailedQueue() {
        return QueueBuilder.durable(INVENTORY_FAILED_Q).build();
    }
}
