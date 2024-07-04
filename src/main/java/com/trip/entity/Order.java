package com.trip.entity;

import com.trip.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
public class Order extends BaseEntity{//예약서
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private  Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private  Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    @OneToMany(mappedBy = "order", cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


    public  void orderCancel(){
        //주문이 취소 되었으므로 주문(order)에서 취소(cancel)로 db에 변경.
        this.orderStatus = OrderStatus.CANCEL;
        //주문서에서 변경하므로 해당 주문서에 들어있는 아이템들도 취소가 될 수 있게 반복문을 돌린다.
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
}
