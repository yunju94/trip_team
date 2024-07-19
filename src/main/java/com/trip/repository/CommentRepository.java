package com.trip.repository;

import com.trip.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByQuestionId(Long questionId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.question.id = :questionId")
    void deleteByQuestionId(@Param("questionId") Long questionId);
}
