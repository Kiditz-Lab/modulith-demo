package com.example.demo.shared;

import org.springframework.modulith.events.Externalized;

import java.util.Set;
@Externalized(Constant.ORDERS)
public record OrderPlacedEvent(Long orderId, Set<LineItemDto> items) {
}
