package com.trip.controller;

import com.trip.entity.Member;
import com.trip.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


//    @GetMapping(value = "/orders")
//    public String order(Principal principal, Model model){
//        String email = principal.getName();
//
//
//        model.addAttribute("");
//        return "/order/orderlist";
//    }

}
