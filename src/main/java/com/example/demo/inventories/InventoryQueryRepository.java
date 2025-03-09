package com.example.demo.inventories;

import org.springframework.data.repository.ListPagingAndSortingRepository;

interface InventoryQueryRepository extends ListPagingAndSortingRepository<Inventory, Long> {
}
