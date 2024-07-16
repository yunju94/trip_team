package com.trip.repository;

import com.trip.entity.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionsRepository extends JpaRepository<Questions,Long> {
    List<Questions> findByWriter(String name);
}
