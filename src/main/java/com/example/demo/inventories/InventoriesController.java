package com.example.demo.inventories;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventories")
@Tag(name = "Inventory")
@RequiredArgsConstructor
public class InventoriesController {
	private final Inventories inventories;

	@PostMapping
	InventoryDto updateStock(@RequestBody UpdateStockCommand command) {
		return inventories.updateStock(command);
	}

	@PatchMapping("/adjust")
	InventoryDto adjustStock(@RequestBody AdjustStockCommand command) {
		return inventories.adjustStock(command);
	}
}
