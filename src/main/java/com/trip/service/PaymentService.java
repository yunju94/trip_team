package com.trip.service;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.trip.dto.PaymentCallbackRequest;
import com.trip.dto.RequestPayDto;
import com.trip.entity.Order;

public interface PaymentService {
    // 결제 요청 데이터 조회
    RequestPayDto findRequestDto(Long id);
    // 결제(콜백)
    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request);

    com.trip.entity.Payment paymentSearch(String UId);


}
