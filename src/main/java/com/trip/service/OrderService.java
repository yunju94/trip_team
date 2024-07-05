package com.trip.service;

import com.trip.dto.OrderHistDto;
import com.trip.dto.OrderItemDto;
import com.trip.entity.ItemImg;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.entity.OrderItem;
import com.trip.repository.ItemImgRepository;
import com.trip.repository.MemberRepository;
import com.trip.repository.OrderItemRepository;
import com.trip.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private  final ItemImgRepository itemImgRepository;

    private  final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<OrderHistDto> orderlist(String email, Pageable pageable) {
        //orderhist에 필요한 것. 주문 리스트(가격), 총 갯수, 이미지파일, item이름
        List<Order> orders = orderRepository.findOrders(email, pageable);
        //이메일로 주문 리스트 뽑아내기 + 갯수에 따라 페이지로 만들기

        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        for (Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems){
                ItemImg itemImg = itemImgRepository.findByItemIdAndReqImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);

        }
        Long totalCount = orderRepository.countOrder(email); //주문 총 갯수


      return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);

    }

    public void  orderCancel(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        //오더 레포지토리에 아이디를 넣어서 해당 오더를 찾는다.
        //해당 오더의 ordercancel로 이동한다.
        order.orderCancel();

    }

    public Optional<Order> orderdetail(Long orderId){
        Optional<Order> order=orderRepository.findById(orderId);
        return order;
    }

    public  Optional<OrderItem> orderItemDetail(Optional<Order> order){
        return Optional.ofNullable(orderItemRepository.findById(order.get().getId()).orElseThrow(EntityNotFoundException::new));
    }

    public  Optional<Order> orderfind(String email){
        Member member = memberRepository.findByEmail(email);
        Optional<Order> order = orderRepository.findById(member.getId());
        return order;
    }


}
