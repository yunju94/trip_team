package com.trip.repository;

import com.trip.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> memberSearch(Long id, String name);
}
