package com.example.demo.orders;

import java.util.Set;

public record OrderPlacedEvent(Long orderId, Set<LineItemDto> items) {
}
