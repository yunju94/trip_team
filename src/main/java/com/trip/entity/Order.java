package com.trip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order")
@Setter
@Getter
public class Order {//예약서
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private  Long id;

}
