package com.example.demo.inventories;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface InventoryRepository extends CrudRepository<Inventory, Long> {
	Optional<Inventory> findBySku(String sku);
	@Modifying
	@Query("UPDATE inventory SET quantity = :quantity WHERE sku = :sku")
	void updateStockBySku(String sku, int quantity);

	@Modifying
	@Query("UPDATE inventory SET quantity = quantity + :quantity  WHERE sku = :sku")
	void adjustStockBySku(String sku, int quantity);

}