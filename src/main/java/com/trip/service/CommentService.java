package com.trip.service;

import com.trip.dto.CommentDto;
import com.trip.entity.Comment;
import com.trip.entity.Questions;
import com.trip.repository.CommentRepository;
import com.trip.repository.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private QuestionsRepository questionsRepository;

    public void save(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setWriter(commentDto.getWriter());
        comment.setContent(commentDto.getContent());
        Questions question = questionsRepository.findById(commentDto.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id: " + commentDto.getQuestionId()));
        comment.setQuestion(question);
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByQuestionId(Long questionId) {
        return commentRepository.findByQuestionId(questionId);
    }
    @Transactional
    public void deleteCommentsByQuestionId(Long questionId) {
        commentRepository.deleteByQuestionId(questionId);
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

}
