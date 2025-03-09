package com.example.demo.shared;

import java.util.Set;

public record StockReservedEvent(Long orderId, Set<String> skus) {
}
