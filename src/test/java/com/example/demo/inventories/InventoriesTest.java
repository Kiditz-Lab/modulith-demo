package com.example.demo.inventories;

import com.example.demo.shared.LineItemDto;
import com.example.demo.shared.OrderPlacedEvent;
import com.example.demo.shared.StockReservedEvent;
import com.example.demo.shared.StockReservedEventFailed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoriesTest {

	@Mock
	private InventoryRepository repository;

	@Mock
	private ApplicationEventPublisher publisher;

	@InjectMocks
	private Inventories inventories;

	@Captor
	private ArgumentCaptor<StockReservedEvent> stockReservedEventCaptor;

	@Captor
	private ArgumentCaptor<StockReservedEventFailed> stockReservedEventFailedCaptor;

	private final String SKU = "ABC123";

	@BeforeEach
	void setUp() {
		reset(repository, publisher);
	}

	@Test
	void shouldUpdateStock_WhenSkuExists() {
		// Given
		UpdateStockCommand command = new UpdateStockCommand(SKU, 10);
		Inventory existingInventory = new Inventory(1L, SKU, 5);

		when(repository.findBySku(SKU)).thenReturn(Optional.of(existingInventory));
		when(repository.findBySku(SKU)).thenReturn(Optional.of(new Inventory(1L, SKU, 15)));

		// When
		InventoryDto result = inventories.updateStock(command);

		// Then
		assertEquals(15, result.quantity());
		verify(repository).updateStockBySku(SKU, 10);
	}

	@Test
	void shouldCreateNewInventory_WhenSkuDoesNotExist() {
		// Given
		UpdateStockCommand command = new UpdateStockCommand(SKU, 10);

		when(repository.findBySku(SKU)).thenReturn(Optional.empty());
		when(repository.save(any())).thenReturn(new Inventory(1L, SKU, 10));

		// When
		InventoryDto result = inventories.updateStock(command);

		// Then
		assertNotNull(result.id());
		assertEquals(10, result.quantity());
		verify(repository).save(any());
	}

	@Test
	void shouldAdjustStock_WhenSkuExists() {
		// Given
		AdjustStockCommand command = new AdjustStockCommand(SKU, -2);
		Inventory existingInventory = new Inventory(1L, SKU, 10);

		when(repository.findBySku(SKU)).thenReturn(Optional.of(existingInventory));
		when(repository.findBySku(SKU)).thenReturn(Optional.of(new Inventory(1L, SKU, 8)));

		// When
		InventoryDto result = inventories.adjustStock(command);

		// Then
		assertEquals(8, result.quantity());
		verify(repository).adjustStockBySku(SKU, -2);
	}

	@Test
	void shouldThrowException_WhenAdjustingStockForNonExistentSku() {
		// Given
		AdjustStockCommand command = new AdjustStockCommand(SKU, -2);
		when(repository.findBySku(SKU)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(InventoryException.class, () -> inventories.adjustStock(command));
	}

	@Test
	void shouldReserveStock_WhenStockIsAvailable() {
		// Given
		OrderPlacedEvent event = new OrderPlacedEvent(1L, Set.of(new LineItemDto(SKU, 2)));
		Inventory inventory = new Inventory(1L, SKU, 10);

		when(repository.findBySku(SKU)).thenReturn(Optional.of(inventory));

		// When
		inventories.on(event);

		// Then
		verify(publisher).publishEvent(stockReservedEventCaptor.capture());
		StockReservedEvent reservedEvent = stockReservedEventCaptor.getValue();

		assertEquals(1L, reservedEvent.orderId());
		assertEquals(Set.of(SKU), reservedEvent.skus());
	}

	@Test
	void shouldPublishStockReservedEventFailed_WhenStockIsInsufficient() {
		// Given
		OrderPlacedEvent event = new OrderPlacedEvent(1L, Set.of(new LineItemDto(SKU, 20)));
		Inventory inventory = new Inventory(1L, SKU, 10);

		when(repository.findBySku(SKU)).thenReturn(Optional.of(inventory));

		// When
		inventories.on(event);

		// Then
		verify(publisher).publishEvent(stockReservedEventFailedCaptor.capture());
		StockReservedEventFailed failedEvent = stockReservedEventFailedCaptor.getValue();

		assertEquals(1L, failedEvent.orderId());
		assertTrue(failedEvent.reason().contains("Insufficient stock"));
	}

	@Test
	void shouldPublishStockReservedEventFailed_WhenStockNotFound() {
		// Given
		OrderPlacedEvent event = new OrderPlacedEvent(1L, Set.of(new LineItemDto(SKU, 5)));

		when(repository.findBySku(SKU)).thenReturn(Optional.empty()); // Simulating stock not found

		// When
		inventories.on(event);

		// Then
		verify(publisher).publishEvent(stockReservedEventFailedCaptor.capture());
		StockReservedEventFailed failedEvent = stockReservedEventFailedCaptor.getValue();

		assertEquals(1L, failedEvent.orderId());
		assertEquals("Sku not found", failedEvent.reason());
	}

}
