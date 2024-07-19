package com.trip.service;




import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;


import java.util.HashMap;

public class TestCoolSms {
    public static void certifiedPhoneNumber(String phoneNumber, String cerNum) {
        String api_key = "NCSSGHQ6HNPMRWDG"; // 여기에 실제 API 키를 입력하세요
        String api_secret = "560HHVNCJGKZMKUPASZLXYYFP7O2RTAO"; // 여기에 실제 API 시크릿을 입력하세요
        Message coolsms = new Message(api_key, api_secret);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);
        params.put("from", "01085191350"); // 여기에는 실제 발신 번호를 입력하세요
        params.put("type", "SMS");
        params.put("text", "더조은투어 인증번호는 [" + cerNum + "] 입니다. 감사합니다.");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
