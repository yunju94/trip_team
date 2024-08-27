package com.trip.repository;

import com.trip.entity.event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends  JpaRepository<event, Long> , EventRepositoryCustom{
}
