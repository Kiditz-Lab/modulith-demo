package com.example.demo.orders;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class OrderController {
	private final Orders orders;

	OrderController(Orders orders) {
		this.orders = orders;
	}

	@PostMapping
	void placeOrder(@RequestBody OrderPlacedCommand command) {
		orders.placed(command);
	}
}
