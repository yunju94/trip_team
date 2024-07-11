package com.trip.repository;


import com.trip.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeNatureRepository extends JpaRepository<Exchange, Long> {
}
