package com.trip.repository;

import com.trip.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByItemId(Long itemId);

    Optional<Review> findByMemberIdAndItemId(Long memberId, Long ItemId);
}
