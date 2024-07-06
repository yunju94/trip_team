package com.trip.controller;


import com.trip.constant.Role;
import com.trip.dto.ItemFormDto;
import com.trip.dto.OrderDto;
import com.trip.dto.OrderHistDto;
import com.trip.dto.OrderItemDto;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.entity.OrderItem;
import com.trip.service.ItemService;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    @PostMapping(value = "/order")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult
            , Principal principal){
        //string a = "abc" + "def"
        //stringBuilder a;
        //a.append("abc")
        //a.append("def")
        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        //로그인 정보 -> sprinf security
        //principal.gerName() (현재 로그인된 정보)
        String email = principal.getName();
        Long orderId;
        try{
            orderId = orderService.order(orderDto, email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
        return  new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


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

        return "mypage/mypageOrder";
    }

    @PostMapping(value = "/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity orderCancel(@PathVariable("orderId") Long orderId, Principal principal){
        if (principal ==null){

            return new ResponseEntity<String>("로그인 후 이용하시기 바랍니다.", HttpStatus.BAD_REQUEST);
        }

        try{
            orderService.orderCancel(orderId);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
        return  new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = "/order/detail/{orderId}")
    public String orderDetail(@PathVariable("page")Optional<Integer> page, @PathVariable("orderId") Long orderId,Principal principal, Model model){
        //오더 아이디를 받는다. 개인정보를 받는다. 오더 아이디로 저장된 오더 정보를 불러온다.
        Optional<Order> order = orderService.orderdetail(orderId);

        List<OrderHistDto> OrderHistDto = orderService.order(principal.getName());
        Optional<OrderItem> orderItem = orderService.orderItemDetail(order);
        ItemFormDto itemFormDto =itemService.getItemDtl(orderItem.get().getItem().getId());

        //오더 중에서도 오더 아이템 정보를 불러온다.
        //해당 정보를 가지고 html로 간다.

        // 이메일을 가지고 service에 가서 개인 정보를 이용해서 order리스트를 받아온다.
        //html에 오더 리스트를 넘겨주고 for문으로 돌려서 찾는다.


        model.addAttribute("OrderHistDto", OrderHistDto);

        model.addAttribute("item", itemFormDto);

        return "mypage/orderdetail";
    }


}
