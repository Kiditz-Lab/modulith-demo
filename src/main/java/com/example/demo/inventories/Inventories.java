package com.example.demo.inventories;

import com.example.demo.shared.LineItemDto;
import com.example.demo.shared.OrderPlacedEvent;
import com.example.demo.shared.StockReservedEvent;
import com.example.demo.shared.StockReservedEventFailed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
class Inventories {
	private final InventoryRepository repository;
	private final ApplicationEventPublisher publisher;

	InventoryDto updateStock(UpdateStockCommand command) {
		var inventory = repository.findBySku(command.sku()).orElse(new Inventory(null, command.sku(), command.quantity()));
		if (inventory.id() == null) {
			inventory = repository.save(inventory);
		} else {
			repository.updateStockBySku(command.sku(), command.quantity());
			inventory = repository.findBySku(command.sku()).orElse(new Inventory(null, command.sku(), command.quantity()));
		}
		return new InventoryDto(inventory.id(), inventory.sku(), inventory.quantity());
	}

	InventoryDto adjustStock(AdjustStockCommand command) {
		repository.adjustStockBySku(command.sku(), command.quantity());
		var inventory = repository.findBySku(command.sku()).orElseThrow(() -> new InventoryException("Sku not found"));
		return new InventoryDto(inventory.id(), inventory.sku(), inventory.quantity());
	}

	@ApplicationModuleListener
	void on(OrderPlacedEvent event) {
		log.info("Start Event captured: {}", event);
		try {

			for (LineItemDto item : event.items()) {
				var inventory = repository.findBySku(item.sku()).orElseThrow(() -> new InventoryException("Sku not found"));
				if (inventory.quantity() < item.quantity()) {
					throw new InventoryException("Insufficient stock for sku %s".formatted(item.sku()));
				}
				adjustStock(new AdjustStockCommand(item.sku(), item.quantity() * -1));
			}
			publisher.publishEvent(new StockReservedEvent(event.orderId(), event.items().stream().map(LineItemDto::sku).collect(Collectors.toSet())));
		} catch (InventoryException e) {
			publisher.publishEvent(new StockReservedEventFailed(event.orderId(), e.getMessage()));
		}

		log.info("End Event captured: {}", event);
	}

}
