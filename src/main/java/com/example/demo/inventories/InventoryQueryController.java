package com.example.demo.inventories;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventories")
@Tag(name = "Inventory")
@RequiredArgsConstructor
class InventoryQueryController {
	private final InventoriesQuery query;
	@GetMapping
	Page<Inventory> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return query.findAll(pageable);
	}
}
