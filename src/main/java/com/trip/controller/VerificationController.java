package com.trip.controller;

import com.trip.service.TestCoolSms;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/verification")
public class VerificationController {
    private final Map<String, String> phoneVerificationCodes = new HashMap<>();

    @PostMapping("/sendVerificationCode")
    @ResponseBody
    public String sendVerificationCode(@RequestParam String phoneNumber) {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr.append(ran);
        }
        String verificationCode = numStr.toString();
        phoneVerificationCodes.put(phoneNumber, verificationCode);

        // 실제로 SMS를 보내는 부분
        TestCoolSms.certifiedPhoneNumber(phoneNumber, verificationCode);

        return "인증번호가 전송되었습니다.";
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public String verifyCode(@RequestParam String phoneNumber, @RequestParam String verificationCode) {
        String storedCode = phoneVerificationCodes.get(phoneNumber);
        if (storedCode != null && storedCode.equals(verificationCode)) {
            return "확인되셨습니다.";
        } else {
            return "올바른 인증번호를 입력 해 주세요.";
        }
    }
}
