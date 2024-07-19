package com.trip.dto;


import com.trip.constant.Category;
import com.trip.constant.Nature;
import com.trip.constant.OrderStatus;
import com.trip.entity.Item;
import com.trip.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private  String itemNm;//상품명

    ///////////////////////////item////////////////////
    private LocalDate startDate; // 출발 날짜

    private LocalDate endDate; // 도착 날짜

    private Nature nature; // 국내 해외

    private Category category; // 지역

    private  String itemDetail; //상세 상품 설명





   public OrderHistDto(Order order, Item item ) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();

        //////////item/////////
        this.category = item.getCategory();
        this.nature = item.getNature();
        this.startDate = item.getStartDate();
        this.endDate = item.getEndDate();
        this.itemDetail = item.getItemDetail();


 }
    public void addOrderItemDto(OrderItemDto orderItemDto){
       orderItemDtoList.add(orderItemDto);
   }
}
