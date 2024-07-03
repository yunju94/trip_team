package com.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findByTel(String tel);
}
