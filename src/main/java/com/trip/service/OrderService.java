package com.trip.service;

import com.trip.dto.OrderDto;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.repository.OrderItemRepository;
import com.trip.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private  final OrderItemRepository orderItemRepository;
    private  final OrderRepository orderRepository;

//    public  void orderlist(String email){
//        //이메일로 주문 리스트 뽑아내기
//      List<Order> orderList = orderRepository.findOrders(email);
//
//
//    }


}
