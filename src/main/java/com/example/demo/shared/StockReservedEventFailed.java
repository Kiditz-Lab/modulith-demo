package com.example.demo.shared;

import org.springframework.modulith.events.Externalized;

@Externalized(Constant.INVENTORY_FAILED)
public record StockReservedEventFailed(Long orderId, String reason) {
}