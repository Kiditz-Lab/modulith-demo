package com.example.demo.orders;

import org.springframework.stereotype.Service;

@Service
class Orders {
	private final OrderRepository repository;

	Orders(OrderRepository repository) {
		this.repository = repository;
	}

	void placed(Order order) {
		repository.save(order);
	}
}
