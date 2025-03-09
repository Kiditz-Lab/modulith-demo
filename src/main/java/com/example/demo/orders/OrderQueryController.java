package com.example.demo.orders;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
@RequiredArgsConstructor
class OrderQueryController {
	private final OrdersQuery query;
	@GetMapping
	Page<Order> getOrders(@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return query.getOrders(pageable);
	}
}
