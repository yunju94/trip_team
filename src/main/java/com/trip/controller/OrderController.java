package com.trip.controller;


import com.trip.constant.Role;
import com.trip.dto.OrderDto;
import com.trip.dto.OrderHistDto;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.entity.OrderItem;
import com.trip.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(value = {"/orders" , "/orders/{page}"})
    public String ordergo(@PathVariable("page")Optional<Integer> page, Principal principal, Model model){

        if (principal ==null){
            model.addAttribute("errorMessage", "로그인 후 이용하시기 바랍니다.");
            return "member/memberLoginForm";
        }
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 10);
        //페이지가 있는가? 없으면 [0]페이지를 받는다. 있으면 그 페이지를 얻는다. 페이지당 크기 10

        Page<OrderHistDto> orderHistDtoList = orderService.orderlist(principal.getName(), pageable);
        // 이메일을 가지고 service에 가서 개인 정보를 이용해서 order리스트를 받아온다.
        //html에 오더 리스트를 넘겨주고 for문으로 돌려서 찾는다.
        model.addAttribute("orderlist", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 10);

        return "order/orderlist";
    }

    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity orderCancel(@PathVariable("oderId") Long orderId, Principal principal){

        orderService.orderCancel(orderId);
        return  new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = "/order/detail/{orderId}")
    public String orderDetail(@Valid Long orderId,Principal principal, Model model){
        //오더 아이디를 받는다. 개인정보를 받는다. 오더 아이디로 저장된 오더 정보를 불러온다.
        Optional<Order> order = orderService.orderdetail(orderId);
        Optional<OrderItem> orderItem = orderService.orderItemDetail(order);
        //오더 중에서도 오더 아이템 정보를 불러온다.
        //해당 정보를 가지고 html로 간다.
        model.addAttribute("orderItem", orderItem);

        return "order/orderdetail";
    }


}
