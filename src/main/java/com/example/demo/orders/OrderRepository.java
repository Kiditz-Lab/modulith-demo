package com.example.demo.orders;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

interface OrderRepository extends CrudRepository<Order, Long> {
	@Modifying
	@Query("UPDATE orders SET status = :status, reason = :reason WHERE id = :id")
	void updateStatusAndReasonById(Long id, OrderStatus status, String reason);
}
