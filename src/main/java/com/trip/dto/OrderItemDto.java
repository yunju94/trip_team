package com.trip.dto;

import com.trip.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private  Long id;
    private  String orderprice;
    private  int count;
    private  String imgUrl;
    private String itemNm;
    public OrderItemDto(OrderItem orderItem, String imgUrl){
                this.itemNm = orderItem.getItem().getItemNm();
                this.count = orderItem.getCount();
                this.orderprice = orderItem.getOrderprice();
                this.imgUrl = imgUrl;
    }

}
