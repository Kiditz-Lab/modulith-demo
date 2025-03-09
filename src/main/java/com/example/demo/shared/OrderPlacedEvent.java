package com.example.demo.shared;

import java.util.Set;

public record OrderPlacedEvent(Long orderId, Set<LineItemDto> items) {
}
