package com.example.demo.orders;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Orders")
@RequiredArgsConstructor
class OrderController {
	private final Orders orders;

	@PostMapping
	void placeOrder(@RequestBody OrderPlacedCommand command) {
		orders.placed(command);
	}
}
