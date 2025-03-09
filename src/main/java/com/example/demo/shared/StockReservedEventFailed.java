package com.example.demo.shared;

public record StockReservedEventFailed(Long orderId, String reason) {
}