package com.trip.controller;

import com.trip.dto.ItemFormDto;
import com.trip.dto.MemberFormDto;
import com.trip.dto.OrderHistDto;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.entity.OrderItem;
import com.trip.service.MemberService;
import com.trip.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MypageController {
    private final OrderService orderService;
    private  final MemberService memberService;

    @GetMapping(value = "/mypage")
    public String mypageOpen( Principal principal, Model model){
      //오더 아이디를 받는다. 개인정보를 받는다. 오더 아이디로 저장된 오더 정보를 불러온다.
        Optional<Order> order =  orderService.orderfind(principal.getName());
        if (order.isEmpty()){
            return "mypage/mypageOrder";
        }
        Optional<OrderItem> orderItem = orderService.orderItemDetail(order);
        //오더 중에서도 오더 아이템 정보를 불러온다.
        //해당 정보를 가지고 html로 간다
        model.addAttribute("orderItem", orderItem);
        return "mypage/mypageOrder";
    }

    @GetMapping(value = "/updateInfo")
    public  String updateInformation(Principal principal, Model model){
        Member member = memberService.memberload(principal.getName());
        model.addAttribute("memberFormDto", member);
        return "mypage/updateInfo";
    }
    @PostMapping(value = "/update/Info")
    public  String updateInformationPost(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "에러가 발생했습니다.");

        }
        try {
            memberService.updateMember(memberFormDto);
            model.addAttribute("errorMessage", "완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "수정 중 에러가 발생하였습니다.");


        }

        return "mypage/updateInfo";

    }

}
