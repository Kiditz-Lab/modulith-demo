package com.example.demo.inventories;

import java.util.Set;

public record StockReservedEvent(Long orderId, Set<String> skus) {
}
