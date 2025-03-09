package com.example.demo.orders;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("orders")
record Order(@Id Long id, Set<LineItem> items, OrderStatus status) {
}

enum OrderStatus {
	PENDING, COMPLETED, FAILED
}


@Table("items")
record LineItem(@Id Long id, String sku, int quantity) {
}