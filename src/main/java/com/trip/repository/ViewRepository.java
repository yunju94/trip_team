package com.trip.repository;

import com.trip.entity.Viewer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewRepository extends JpaRepository<Viewer, Long> {

    Viewer findByMemberIdAndItemId(Long memberId, Long ItemId);

    List<Viewer> findByMemberIdOrderByCountAsc(Long memberId);

    Viewer findByItemId(Long ItemId);

}
