package com.example.demo.orders;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
class Orders {
	private final OrderRepository repository;

	Orders(OrderRepository repository) {
		this.repository = repository;
	}

	void placed(OrderPlacedCommand command) {
		Set<LineItem> itemSet = command.items().stream().map(item -> new LineItem(null, item.sku(), item.quantity())).collect(Collectors.toSet());
		Order order = new Order(null, itemSet, OrderStatus.PENDING);
		repository.save(order);
	}
}
