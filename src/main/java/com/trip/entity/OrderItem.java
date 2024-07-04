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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private  Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private  Item item;

    public  void  cancel(){
        //주문서안의 아이템이 취소가 되면 아이템의 갯수가 다시 증가해야한다. 그러므로 아이템의 갯수를 증가시켜야한다.
        this.getItem().cancelAdditem(count);
    }








}
