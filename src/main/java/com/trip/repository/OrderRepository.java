package com.trip.repository;

import com.trip.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
//    List<Order> findOrders(@Param("email") String email);
}
