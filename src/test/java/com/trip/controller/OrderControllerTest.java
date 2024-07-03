package com.trip.controller;

import com.trip.constant.OrderStatus;
import com.trip.dto.MemberFormDto;
import com.trip.dto.OrderDto;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.service.OrderService;
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


    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("인천");
        memberFormDto.setEmail("a@naver.com");
        memberFormDto.setTel("010");
        memberFormDto.setPassword("1234");
        return  Member.createMember((memberFormDto), passwordEncoder);
    }


    public Order createorder(){
        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(createMember().getId());
        orderDto.setCount(2);
        return  new Order(orderDto.getItemId(), createMember(), LocalDateTime.now(), OrderStatus.SELL);
    }

    @Test
    @DisplayName("상품 접근 테스트")
    public  void  Test() throws  Exception{
       Member member=createMember();
       Order order1 = new Order(member.getId(), createMember(), LocalDateTime.now(), OrderStatus.SELL);
       Order order = createorder();
       order1.setOrderStatus(OrderStatus.SELL);
       order1.setMember(member);
       order1.setId(member.getId());
       order1.setOrderDate(LocalDateTime.now());
       List<Order> orderList = orderService.orderlist(member.getEmail());
       orderList.add(order);
       assertEquals(order1.getId(),orderList.get(0).getId());
    }


}