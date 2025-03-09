package com.example.demo.orders;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OrderAmqpConfiguration {
    public static final String ORDER_Q = "orders";

    @Bean
    Binding binding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ORDER_Q).noargs();
    }

    @Bean
    Exchange exchange() {
        return ExchangeBuilder.directExchange(ORDER_Q).build();
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(ORDER_Q).build();
    }
}
