package com.trip.controller;


import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.trip.dto.PaymentCallbackRequest;
import com.trip.dto.RequestPayDto;
import com.trip.entity.Order;
import com.trip.service.OrderService;
import com.trip.service.PaymentService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;


    @GetMapping("/payment/{id}")
    public String paymentPage(@PathVariable(name = "id", required = false) Long id, Model model) {
        System.out.println(id);

        RequestPayDto requestDto = paymentService.findRequestDto(id);

        model.addAttribute("requestDto", requestDto);
        return "order/payment";
    }

    @ResponseBody
    @PostMapping("/payment")
    public ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);


        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
    }

    @GetMapping("/success-payment")
    public String successPaymentPage() {
        return "order/success-payment";
    }

    @GetMapping("/fail-payment/{orderUid}")
    public String failPaymentPage(@PathVariable("orderUid") String orderUid) {
                                            //파라미터 값이 없으면 null값으로

        Order order =orderService.orderUidOrderCancle(orderUid);



        return "order/fail-payment";
    }
}
