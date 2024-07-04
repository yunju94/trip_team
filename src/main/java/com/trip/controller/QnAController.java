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
                return "여행 예약은 고객센터를 통해 가능합니다.";
            case 2:
                return "여행 취소 정책은 예약 상황에 따라 다르니 고객센터에서 문의 바랍니다.";
            case 3:
                return "여행 중에 도움이 필요하시면 고객센터 또는 현지 가이드에게 문의하세요.";
            case 4:
                return "여행중에 생긴 분실물은 저희 여행사에서는 책임을 지지 않습니다.";
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
