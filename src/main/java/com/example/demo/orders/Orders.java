package com.example.demo.orders;

import com.example.demo.shared.OrderPlacedEvent;
import com.example.demo.shared.StockReservedEvent;
import com.example.demo.shared.StockReservedEventFailed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
class Orders {
	private final OrderRepository repository;
	private final ApplicationEventPublisher publisher;

	void placed(OrderPlacedCommand command) {
		Set<LineItem> itemSet = command.items().stream().map(item -> new LineItem(null, item.sku(), item.quantity())).collect(Collectors.toSet());
		var order = new Order(null, itemSet, OrderStatus.PENDING);
		order = repository.save(order);
		publisher.publishEvent(new OrderPlacedEvent(order.id(), command.items()));
	}

	@ApplicationModuleListener
	void on (StockReservedEventFailed event){
		log.info("Start reservation failed");
		repository.updateStatusAndReasonById(event.orderId(), OrderStatus.FAILED, event.reason());
		log.info("End reservation failed");
	}

	@ApplicationModuleListener
	void on (StockReservedEvent event){
		log.info("Start reservation completed");
		repository.updateStatusAndReasonById(event.orderId(), OrderStatus.COMPLETED, "Order completed");
		log.info("End reservation completed");
	}
}
