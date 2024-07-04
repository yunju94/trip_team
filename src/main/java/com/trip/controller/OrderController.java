package com.trip.controller;


import com.trip.constant.Role;
import com.trip.dto.OrderDto;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.entity.OrderItem;
import com.trip.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @GetMapping(value = "/orders")
    public String ordergo(Principal principal, Model model, OrderDto orderDto){
        //로그인이 비어있는 초기에만 대체용
        if (principal==null){
            Member member = new Member();
            member.setId(Long.valueOf(0));
            member.setRole(Role.USER);
            member.setPassword("1234");
            member.setAddress("0");
            member.setTel("010");
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderprice("1,000");
            orderItem.setCount(2);
            orderItem.setId(member.getId());

            model.addAttribute("orderlist", orderItem);
            return "order/orderlist";

        }
        String email = principal.getName();//email정보를 받아온다.
        // 이메일을 가지고 service에 가서 개인 정보를 이용해서 order리스트를 받아온다.
        List<Order> orderList =orderService.orderlist(orderDto, email);
        //html에 오더 리스트를 넘겨주고 for문으로 돌려서 찾는다.
        model.addAttribute("orderlist", orderList);

        return "order/orderlist";
    }


}
