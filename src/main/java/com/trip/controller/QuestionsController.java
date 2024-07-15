package com.trip.controller;

import com.trip.dto.CommentDto;
import com.trip.dto.QuestionsDto;
import com.trip.entity.Comment;
import com.trip.entity.Member;
import com.trip.entity.Questions;
import com.trip.repository.QuestionsRepository;
import com.trip.service.CommentService;
import com.trip.service.MemberService;
import com.trip.service.QuestionsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionsController {
    @Autowired
    QuestionsRepository questionsRepository;
    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberService memberService;


    @GetMapping("/questions")
    public String getQuestionsPage(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        int pageSize = 5; // 페이지당 항목 수 (원하는 숫자로 수정 가능)
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Questions> questionsPage = questionsRepository.findAll(pageable);

        List<Questions> questions = questionsPage.getContent();
        model.addAttribute("questionsList", questions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", questionsPage.getTotalPages());

        return "questions/questions";
    }

    @GetMapping("/writeForm")
    public String writeForm(Model model, Principal principal) {
        Member member = memberService.memberload(principal.getName());
        QuestionsDto questionsDto = new QuestionsDto();
        questionsDto.setWriter(member.getName());
        model.addAttribute("questionsDto", questionsDto);
        return "questions/writeForm";
    }

    @PostMapping("/writeForm")
    public String write(QuestionsDto questionsDto, Principal principal) {
        String email = principal.getName();
        questionsService.save(questionsDto);
        return "redirect:/questions";
    }
    @GetMapping(value ="/view")
    public String viewQuestion(@RequestParam("id") Long id, Model model, Principal principal) {
        Member member = memberService.memberload(principal.getName());
        Questions question = questionsService.getQuestionById(id);
        List<Comment> comments = commentService.getCommentsByQuestionId(id);
        CommentDto commentDto = new CommentDto();
        commentDto.setWriter(member.getName());

        model.addAttribute("question", question);
        model.addAttribute("comments", comments);
        model.addAttribute("commentDto", commentDto);
        return "questions/view";
    }
    @GetMapping(value ="/view/{id}")
    public String viewQuestionId(@PathVariable("id") Long id, Model model, Principal principal) {
        Member member = memberService.memberload(principal.getName());
        Questions question = questionsService.getQuestionById(id);
        List<Comment> comments = commentService.getCommentsByQuestionId(id);
        CommentDto commentDto = new CommentDto();
        commentDto.setWriter(member.getName());

        model.addAttribute("question", question);
        model.addAttribute("comments", comments);
        model.addAttribute("commentDto", commentDto);
        return "questions/view";
    }
}
