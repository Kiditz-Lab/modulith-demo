package com.example.demo.orders;

import org.springframework.data.repository.CrudRepository;

interface OrderRepository extends CrudRepository<Order, Integer> {
}
