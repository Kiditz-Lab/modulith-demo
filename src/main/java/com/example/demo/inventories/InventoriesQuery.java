package com.example.demo.inventories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class InventoriesQuery {
	private final InventoryQueryRepository repository;

	public Page<Inventory> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
}
