package com.trip.controller;

import com.trip.dto.CommentDto;
import com.trip.dto.QuestionsDto;
import com.trip.entity.Comment;
import com.trip.entity.Questions;
import com.trip.service.CommentService;
import com.trip.service.QuestionsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionsController {
    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/questions")
    public String questionsList(Model model) {
        List<Questions> questionsList = questionsService.getAllQuestions();
        model.addAttribute("questionsList", questionsList);
        return "questions/questions";
    }

    @GetMapping("/writeForm")
    public String writeForm(Model model) {
        model.addAttribute("questionsDto", new QuestionsDto());
        return "questions/writeForm";
    }

    @PostMapping("/writeForm")
    public String write(QuestionsDto questionsDto) {
        questionsService.save(questionsDto);
        return "redirect:/questions";
    }
    @GetMapping("/view")
    public String viewQuestion(@RequestParam("id") Long id, Model model) {
        Questions question = questionsService.getQuestionById(id);
        List<Comment> comments = commentService.getCommentsByQuestionId(id);
        model.addAttribute("question", question);
        model.addAttribute("comments", comments);
        model.addAttribute("commentDto", new CommentDto());
        return "questions/view";
    }
}
