package com.trip.controller;

import com.trip.dto.CommentDto;
import com.trip.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/comments")
    public String saveComment(@ModelAttribute CommentDto commentDto) {
        commentService.save(commentDto);
        return "redirect:/view?id=" + commentDto.getQuestionId();
    }
}
