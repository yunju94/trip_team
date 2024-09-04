package com.trip.service;

import com.trip.dto.ReviewFormDto;
import com.trip.entity.ItemImg;
import com.trip.entity.Review;
import com.trip.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private  final  ReviewRepository reviewRepository;
    public void saveReview(ReviewFormDto reviewFormDto){
        Review review = Review.saveReview(reviewFormDto);
        System.out.println("22222222222222222222222222222222222");
        reviewRepository.save(review);
        System.out.println("111111111111111111111111111111111111111");

    }


}
