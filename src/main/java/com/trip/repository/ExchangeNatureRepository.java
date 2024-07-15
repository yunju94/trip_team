package com.trip.repository;


import com.trip.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeNatureRepository extends JpaRepository<Exchange, Long> {
    @Query("select DISTINCT e.PHP from Exchange e where e.date = :date")
    List<String> findByPHP(@Param("date") LocalDate date);

    @Query("select DISTINCT e.JPY from Exchange e where e.date = :date")
    List<String> findByJPY(@Param("date") LocalDate date);
    @Query("select DISTINCT e.USD from Exchange e where e.date = :date")
    List<String> findByUSD(@Param("date") LocalDate date);
    @Query("select DISTINCT e.VND from Exchange e where e.date = :date")
    List<String> findByVND(@Param("date") LocalDate date);
    @Query("select DISTINCT e.MYR from Exchange e where e.date = :date")
    List<String> findByMYR(@Param("date") LocalDate date);

}
