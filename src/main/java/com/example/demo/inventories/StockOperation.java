package com.example.demo.inventories;


sealed interface StockOperation permits AdjustStockCommand, UpdateStockCommand {
	String sku();

	int quantity();
}

record AdjustStockCommand(String sku, int quantity) implements StockOperation {
}

record UpdateStockCommand(String sku, int quantity) implements StockOperation {
}