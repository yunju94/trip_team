package com.trip.service;

import com.trip.dto.QuestionsDto;
import com.trip.entity.Questions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionsService {


    public void save(QuestionsDto questionsDto) {
        Questions questions = new Questions();
        questions.setWriter(questionsDto.getWriter());
        questions.setTitle(questionsDto.getTitle());
        questions.setContent(questionsDto.getContent());
        // 필요한 로직 추가
    }
}
