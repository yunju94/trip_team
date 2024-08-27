package com.trip.repository;

import com.trip.dto.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface EventRepositoryCustom {

    Page<EventDto> getEventJapan(Pageable pageable, String search);

    Page<EventDto> getEventDate(Pageable pageable, LocalDate startDate, LocalDate endDate);

}
