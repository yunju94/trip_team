package com.trip.service;

import com.trip.dto.OrderDto;
import com.trip.dto.OrderItemDto;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.entity.OrderItem;
import com.trip.repository.MemberRepository;
import com.trip.repository.OrderItemRepository;
import com.trip.repository.OrderRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.results.complete.EntityResultImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private  final OrderItemRepository orderItemRepository;
    private  final OrderRepository orderRepository;

    private  final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public  List<Order> orderlist(OrderDto orderDto,String email){
       Order order = orderRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityExistsException::new);
        //오더 아이템을 통해 아이템을 뽑아낸다.
        Member member = memberRepository.findByEmail(email);
        //이메일로 주문 리스트 뽑아내기
      List<Order> orderList = orderRepository.findOrders(email);
      OrderItem orderItem = orderItemRepository.findByOrderIdAndItemId(order.getId(), member.getId());
      orderList.add(orderItem.getOrder());
      return orderList;

    }



}
