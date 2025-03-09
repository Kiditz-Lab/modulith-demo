package com.example.demo.orders;

import com.example.demo.shared.LineItemDto;
import com.example.demo.shared.OrderPlacedEvent;
import com.example.demo.shared.StockReservedEvent;
import com.example.demo.shared.StockReservedEventFailed;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersTest {
	private final Faker faker = new Faker();

	@Mock
	OrderRepository repository;

	@Mock
	ApplicationEventPublisher publisher;

	@InjectMocks
	Orders orders;

	@Test
	void shouldPlaceOrderAndPublishEvent() {
		// Arrange
		String sku = faker.letterify("???") + "-" + faker.numerify("#####");
		int quantity = faker.number().numberBetween(1, 10);

		var itemDtos = Set.of(new LineItemDto(sku, quantity));
		Set<LineItem> items = itemDtos.stream()
				.map(item -> new LineItem(null, item.sku(), item.quantity()))
				.collect(Collectors.toSet());

		var command = new OrderPlacedCommand(itemDtos);
		var savedOrder = new Order(1L, items, OrderStatus.PENDING, "");

		when(repository.save(any(Order.class))).thenReturn(savedOrder);

		// Act
		orders.placed(command);

		// Assert
		verify(repository).save(any(Order.class));

		ArgumentCaptor<OrderPlacedEvent> eventCaptor = ArgumentCaptor.forClass(OrderPlacedEvent.class);
		verify(publisher).publishEvent(eventCaptor.capture());

		OrderPlacedEvent capturedEvent = eventCaptor.getValue();
		assert capturedEvent.orderId().equals(1L);
		assert capturedEvent.items().equals(itemDtos);
	}

	@Test
	void shouldUpdateOrderStatusOnReservationFailure() {
		// Arrange
		String reason = "Insufficient stock";
		long orderId = 1L;

		// Act
		orders.on(new StockReservedEventFailed(orderId, reason));

		// Assert
		verify(repository).updateStatusAndReasonById(orderId, OrderStatus.FAILED, reason);
	}

	@Test
	void shouldUpdateOrderStatusOnReservationCompleted() {
		// Arrange
		long orderId = 1L;
		var reservedItems = Set.of("IPHONE_11");

		// Act
		orders.on(new StockReservedEvent(orderId, reservedItems));

		// Assert
		verify(repository).updateStatusAndReasonById(orderId, OrderStatus.COMPLETED, "Order completed");
	}
}
