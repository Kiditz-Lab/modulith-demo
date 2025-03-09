package com.example.demo.orders;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OrderAmqpConfiguration {
	public static final String ORDER_Q = "orders";

	@Bean
	Binding binding(FanoutExchange orderExchange, Queue orderQueue) {
		return BindingBuilder.bind(orderQueue).to(orderExchange);
	}

	@Bean
	FanoutExchange orderExchange() {
		return ExchangeBuilder.fanoutExchange(ORDER_Q).build();
	}

	@Bean
	Queue orderQueue() {
		return QueueBuilder.durable(ORDER_Q).build();
	}
}
