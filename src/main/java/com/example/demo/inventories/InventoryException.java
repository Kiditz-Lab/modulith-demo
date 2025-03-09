package com.example.demo.inventories;

class InventoryException extends RuntimeException {
	public InventoryException() {
	}

	public InventoryException(String message) {
		super(message);
	}
}
