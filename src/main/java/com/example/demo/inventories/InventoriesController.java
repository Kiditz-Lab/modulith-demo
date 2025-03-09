package com.example.demo.inventories;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventories")
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
