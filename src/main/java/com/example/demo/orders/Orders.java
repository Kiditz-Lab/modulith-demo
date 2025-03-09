package com.example.demo.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class Orders {
	private final OrderRepository repository;

	void placed(OrderPlacedCommand command) {
		Set<LineItem> itemSet = command.items().stream().map(item -> new LineItem(null, item.sku(), item.quantity())).collect(Collectors.toSet());
		Order order = new Order(null, itemSet, OrderStatus.PENDING);
		repository.save(order);
	}
}
