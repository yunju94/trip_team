package com.trip.entity;

import com.trip.constant.PaymentStatus;
import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "payment")
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long price;
    private PaymentStatus status;
    private String paymentUid; // 결제 고유 번호

    @Builder
    public Payment(Long price, PaymentStatus status) {
        this.price = price;
        this.status = status;
    }

    public void changePaymentBySuccess(PaymentStatus status, String paymentUid) {
        this.status = status;
        this.paymentUid = paymentUid;
    }
}