package com.example.demo.orders;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OrdersQuery {
	private final OrderQueryRepository repository;

	public Page<Order> getOrders(Pageable pageable) {
		return repository.findAll(pageable);
	}
}
