package com.trip.repository;

import com.trip.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.member.email = :email")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select o from Order o where o.member.email = :email")
    Order findByOrders(@Param("email") String email);
    //멤버의 이메일과 조인.
    @Query("select count(o) from Order o where o.member.email = :email")
    Long countOrder(@Param("email") String email);





    //포트 원
    @Query("select o from Order o" +
            " left join fetch o.payment p" +
            " left join fetch o.member m" +
            " where o.orderUid = :orderUid")
    Optional<Order> findOrderAndPaymentAndMember(Long orderUid);

    @Query("select o from Order o" +
            " left join fetch o.payment p" +
            " where o.orderUid = :orderUid")
    Optional<Order> findOrderAndPayment(String orderUid);


}
