package com.trip.controller;

import com.trip.constant.OrderStatus;
import com.trip.dto.MemberFormDto;
import com.trip.dto.OrderDto;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.entity.OrderItem;
import com.trip.repository.OrderItemRepository;
import com.trip.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderControllerTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemRepository orderItemRepository;


    public Member createMember(){
        Member member = new Member();
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("인천");
        memberFormDto.setEmail("a@naver.com");
        memberFormDto.setTel("010");
        memberFormDto.setPassword("1234");
        return  Member.createMember((memberFormDto), passwordEncoder);
    }


    public Order createorder(){
        Member member = createMember();
        Order order = new Order(member.getId(),member, LocalDateTime.now(), OrderStatus.SELL);
        return order;
    }

    @Test
    @DisplayName("상품 접근 테스트")
    public void Test() throws  Exception{


    }


}