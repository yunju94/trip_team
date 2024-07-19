package com.trip.dto;

import com.trip.constant.Category;
import com.trip.constant.ItemSellStatus;
import com.trip.constant.Nature;
import com.trip.entity.OrderItem;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderItemDto {
    ////////order item/////////////////
    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl;
    private String itemDetail; // 상품상세설명



    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();

        this.imgUrl = imgUrl;
    }
}
