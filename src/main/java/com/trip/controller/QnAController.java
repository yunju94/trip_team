package com.trip.controller;

import com.trip.dto.QnADto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class QnAController {
    @GetMapping(value = "/qna")
    public String qnaList(Model model) {
        model.addAttribute("qna",new  QnADto());
        return "qna/list";
    }
    @GetMapping("/getAnswer")
    public ResponseEntity<?> getAnswer(@RequestParam Long id) {
        try {
            String answer = findAnswerById(id); // 질문 ID에 따른 답변을 가져오는 메서드 호출
            return ResponseEntity.ok().body(new AnswerResponse(answer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }
    }

    // 질문 ID에 따라 적절한 답변을 반환하는 메서드 (임의의 예시)
    private String findAnswerById(Long id) {
        switch (id.intValue()) {
            case 1:
                return "더조은투어는 고객님의 쾌적한 여행을 위해 45인승 관관버스 차량 기준 1인 2좌석 제도를 실행하고 있습니다. (45인승 관광버스기준 최소 12명, 최대 21명 탑승)<br>" +
                        "<br>" +
                        "쾌적한 여행을 위하여 차내 취식 및 일행 간 대화는 자제 부탁드립니다.<br>" +
                        "<br>" +
                        "<br>" +
                        "<span style='color:red;'>※리무진(우등버스)의 경우 1인 1좌석 제공</span><br>" +
                        "<br>" +
                        "<span style='color:red;'>※상품에 따라 탑승 인원이 상이 할 수 있습니다. 각 상품 상세내역 부분 확인 부탁드립니다.</span>";
            case 2:
                return "▶ 더조은투어의 모든 여행 상품은 지정 좌석제이며 예약 순으로 앞자리부터 배정됩니다.<br>" +
                        "<br>" +
                        "<br>" +
                        "▶ 버스 좌석은 사전 임의 지정 및 개인 요청은 불가합니다.<br>" +
                        "<br>" +
                        "<br>" +
                        "▶ 출발일 당일, 버스 탑승 시 좌석 안내드립니다.";
            case 3:
                return "여행 중에 도움이 필요하시면 고객센터 또는 현지 가이드에게 문의하세요.";
            case 4:
                return "2014년 8월7일부터 모든 국내여행상품은 개인정보 보호법(주민등록번호 처리의제한)등 관련법률에 의거하여 개인정보(주민번호)수집이 불가함에 따라 국내여행자보험가입이 불포함으로 변경되었습니다.<br>" +
                        "<br>" +
                        "<br>" +
                        "단, 여행 일정 중에 이용되는 교통수단(항공,선박,버스전용차량등)은 각각 별도의 보험이 가입되어 있음을 알려드립니다. 여행자보험 가입을 원하실 경우에는 개별가입을 권장합니다.";
            default:
                return "해당 질문에 대한 답변이 준비되어 있지 않습니다.";
        }
    }

    // 답변 객체 클래스
    public static class AnswerResponse {
        private String answer;

        public AnswerResponse(String answer) {
            this.answer = answer;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}
