package com.trip.controller;

import com.trip.dto.QuestionsDto;
import com.trip.service.QuestionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class QuestionsController {
    @Autowired
    private QuestionsService questionsService;
    @GetMapping(value = "/questions")
    public String questionsList() {
        return "questions/questions";
    }
    @GetMapping(value = "/writeForm")
    public String writeForm(){
        return "questions/writeForm";
    }
    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<String> write(@RequestBody QuestionsDto questionsDto) {
        try {
            questionsService.save(questionsDto);
            return ResponseEntity.ok("글이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 등록에 실패했습니다.");
        }
    }

}
