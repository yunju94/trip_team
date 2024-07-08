package com.trip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
public class CartItem extends  BaseEntity{// 찜 목록
    // public(공용) > protected(상속) > default(같은 패키지 내부) > private(클래스 안에서만)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//AI
    @Column(name = "cart_item_id")
    private  Long id;

    private  int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private  Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private  Item item;

    public  static  CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;

    }

    public  void addCount(int count){
        this.count += count;
    }

    public  void  updateCount(int count){
        this.count = count;
        //변경 감지로 인해 DB에서 알아서 작동/수정
    }

}
