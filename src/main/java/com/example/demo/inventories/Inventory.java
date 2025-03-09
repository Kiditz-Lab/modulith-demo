package com.example.demo.inventories;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("inventory")
record Inventory(@Id Long id, String sku, int quantity) {
}

record InventoryDto(Long id, String sku, int quantity) {
}
