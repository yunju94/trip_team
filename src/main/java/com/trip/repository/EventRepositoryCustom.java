package com.trip.repository;

import com.trip.dto.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepositoryCustom {

    Page<EventDto> getEventJapan(Pageable pageable, String search);

}
