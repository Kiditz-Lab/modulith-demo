package com.example.demo.shared;

import org.springframework.modulith.events.Externalized;

import java.util.Set;
@Externalized(Constant.INVENTORY)
public record StockReservedEvent(Long orderId, Set<String> skus) {
}
