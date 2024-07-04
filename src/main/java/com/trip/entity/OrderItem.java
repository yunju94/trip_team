package com.trip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{
    //예약 목록-> 여행 시 한 번에 한 패키지로 예약하므로 one to one
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    private String orderprice;
    private int count;

    @OneToOne
    @JoinColumn(name = "order_id")
    private  Order order;


}
