package com.trip.repository;

import com.trip.entity.Mileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, Long> {
}
