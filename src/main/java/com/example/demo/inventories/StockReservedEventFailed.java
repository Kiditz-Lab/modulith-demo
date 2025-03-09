package com.example.demo.inventories;

public record StockReservedEventFailed(Long orderId,  String reason) {
}