package com.example.demo.orders;

import org.springframework.data.repository.ListPagingAndSortingRepository;

interface OrderQueryRepository extends ListPagingAndSortingRepository<Order, Long> {
}
