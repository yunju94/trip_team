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

    private String orderUid; // 주문 번호

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    @OneToMany(mappedBy = "order", cascade= CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public  Order(){

    }
    public  void orderCancel(){
        //주문이 취소 되었으므로 주문(order)에서 취소(cancel)로 db에 변경.
        this.orderStatus = OrderStatus.CANCEL;
        //주문서에서 변경하므로 해당 주문서에 들어있는 아이템들도 취소가 될 수 있게 반복문을 돌린다.
        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }
    public  static  Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return  order;
        //주문서 생성
        //현재 로그인된 멤버 주문서에 추가
        //주문 아이템 리스트를 반복문을 통해서 주문서에 추가
        //상태는 주문으로 세팅
        //주문 시간은 현재시간으로 세팅
        //주문서 리턴

    }

    //주문서 주문 아이템 리스트에 주문 아이템 주가
    //주문 아이템에 주문서 추가
    public  void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
