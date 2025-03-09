package com.example.demo.inventories;

import com.example.demo.orders.OrderPlacedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
class Inventories {
	private final InventoryRepository repository;

	InventoryDto updateStock(UpdateStockCommand command) {
		var inventory = repository.findBySku(command.sku()).orElse(new Inventory(null, command.sku(), command.quantity()));
		if (inventory.id() == null) {
			inventory = repository.save(inventory);
		} else {
			var res = repository.updateStockBySku(command.sku(), command.quantity());
			log.info("Result: {}", res);
			inventory = repository.findBySku(command.sku()).orElse(new Inventory(null, command.sku(), command.quantity()));
		}
		return new InventoryDto(inventory.id(), inventory.sku(), inventory.quantity());
	}

	InventoryDto adjustStock(AdjustStockCommand command) {
		var inventory = repository.findBySku(command.sku()).orElseThrow(() -> new InventoryException("Sku not found"));
		var res = repository.adjustStockBySku(command.sku(), command.quantity());
		log.info("Result: {}", res);
		inventory = repository.findBySku(command.sku()).orElseThrow(() -> new InventoryException("Sku not found"));
		return new InventoryDto(inventory.id(), inventory.sku(), inventory.quantity());
	}

	@ApplicationModuleListener
	void on(OrderPlacedEvent event) throws InterruptedException {
		log.info("Start event {}", event);
		Thread.sleep(10_000L);
		log.info("End event {}", event);
	}

}
