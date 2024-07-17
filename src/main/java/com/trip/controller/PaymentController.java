package com.trip.controller;


import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.trip.dto.PaymentCallbackRequest;
import com.trip.dto.RequestPayDto;
import com.trip.dto.UseMileage;
import com.trip.entity.Member;
import com.trip.entity.Mileage;
import com.trip.entity.Order;
import com.trip.service.MemberService;
import com.trip.service.MileageService;
import com.trip.service.OrderService;
import com.trip.service.PaymentService;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private  final MemberService memberService;
    private final MileageService mileageService;


    @GetMapping("/payment/{id}")
    public String paymentPage(@PathVariable(name = "id", required = false) Long id, Model model, Principal principal) {
        System.out.println(id);

        Member member = memberService.memberload(principal.getName());
        List<Mileage> mileageList =mileageService.membertoMileage(member.getId());
        RequestPayDto requestDto = paymentService.findRequestDto(id);

        model.addAttribute("requestDto", requestDto);
        model.addAttribute("mileageList", mileageList);
        return "order/payment";
    }



    @PostMapping(value = "/payment")
    public  @ResponseBody ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {

        System.out.println(request);
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);
        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/payment/mileage/{orderUid}")
    public  @ResponseBody ResponseEntity mileageMoneyChange(@PathVariable String orderUid, @RequestBody UseMileage useMileage) {

        Order order = orderService.orderUidOrderCancle(orderUid);
        orderService.payUpdate(order, useMileage.getTotalPrice());
        com.trip.entity.Payment payment = paymentService.paymentSearch(orderUid);
        payment.updatepayment(order);
        Member member = memberService.findMember(order.getMember().getId()).orElseThrow();
        mileageService.save(member, useMileage.getUseMileage());

        return new ResponseEntity<String>(orderUid, HttpStatus.OK);
    }


    @GetMapping("/success-payment/{orderUid}")
    public String successPaymentPage() {
        return "order/success-payment";
    }

    @GetMapping("/fail-payment/{orderUid}")
    public String failPaymentPage(@PathVariable("orderUid") String orderUid, @RequestBody UseMileage useMileage) {
                                            //파라미터 값이 없으면 null값으로

        Order order =orderService.orderUidOrderCancle(orderUid);
        Member member = memberService.findMember(order.getMember().getId()).orElseThrow();
        mileageService.saveCancel(member, useMileage.getUseMileage());

        return "order/fail-payment";
    }


}
