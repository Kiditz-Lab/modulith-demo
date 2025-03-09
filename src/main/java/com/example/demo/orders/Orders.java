package com.example.demo.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
class Orders {
	private final OrderRepository repository;
	private final ApplicationEventPublisher publisher;

	void placed(OrderPlacedCommand command) {
		Set<LineItem> itemSet = command.items().stream().map(item -> new LineItem(null, item.sku(), item.quantity())).collect(Collectors.toSet());
		var order = new Order(null, itemSet, OrderStatus.PENDING);
		repository.save(order);
		publisher.publishEvent(new OrderPlacedEvent(order.id(), command.items()));
	}
}
