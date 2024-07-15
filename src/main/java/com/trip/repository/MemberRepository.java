package com.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Member findByEmail(String email);
    Member findByTel(String tel);

}
