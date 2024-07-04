package com.trip.repository;

import com.trip.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.member.email = :email")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    //멤버의 이메일과 조인.
    @Query("select count(o) from Order o where o.member.email = :email")
    Long countOrder(@Param("email") String email);



}
