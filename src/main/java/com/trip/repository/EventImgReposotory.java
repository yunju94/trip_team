package com.trip.repository;

import com.trip.entity.ItemImg;
import com.trip.entity.eventImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventImgReposotory extends JpaRepository<eventImg, Long> {

    List<eventImg> findByeventIdOrderByIdAsc(Long eventId);
}
