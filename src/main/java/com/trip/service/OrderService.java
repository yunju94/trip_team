package com.trip.service;

import com.trip.dto.OrderDto;
import com.trip.dto.OrderHistDto;
import com.trip.dto.OrderItemDto;
import com.trip.entity.*;
import com.trip.repository.*;
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

import static com.trip.entity.QOrder.order;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private  final OrderItemRepository orderItemRepository;
    private  final OrderRepository orderRepository;
    private  final ItemImgRepository itemImgRepository;
    private  final ItemRepository itemRepository;

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



    @Transactional(readOnly = true)
    public List<OrderHistDto> order(String email) {
        //orderhist에 필요한 것. 주문 리스트(가격), 총 갯수, 이미지파일, item이름
        Order orders = orderRepository.findByOrders(email);
        //이메일로 주문 리스트 뽑아내기 + 갯수에 따라 페이지로 만들기

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

            OrderHistDto orderHistDto = new OrderHistDto(orders);
            List<OrderItem> orderItems = orders.getOrderItems();
            for (OrderItem orderItem : orderItems){
                ItemImg itemImg = itemImgRepository.findByItemIdAndReqImgYn(orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);


        return orderHistDtos;

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
    public Long order(OrderDto orderDto, String email){

        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();
        Optional<Order> orderItemOP = orderRepository.findById(member.getId());

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return  order.getId();
    }

    public Optional<Order> getOrder(Long id){
        Optional<Order> order = orderRepository.findById(id);
        return order;

    }











}
