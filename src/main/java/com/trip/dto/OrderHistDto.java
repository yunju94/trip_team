package com.trip.dto;


import com.trip.constant.OrderStatus;
import com.trip.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {
    private Long orderId; //주문 번호
    private String orderDate; //주문 시간
    private OrderStatus orderStatus;//주문 상태
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
    //오더 아이템 리스트(가격, 인원)
    //상품 이름
    private  String itemNm;

   public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();

 }
    public void addOrderItemDto(OrderItemDto orderItemDto){
       orderItemDtoList.add(orderItemDto);
   }
}
