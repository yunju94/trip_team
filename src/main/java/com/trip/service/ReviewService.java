package com.trip.service;

import com.trip.dto.ItemImgDto;
import com.trip.dto.ReviewFormDto;
import com.trip.entity.ItemImg;
import com.trip.entity.Review;
import com.trip.repository.ItemImgRepository;
import com.trip.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private  final  ReviewRepository reviewRepository;
    private final ItemImgService itemImgService;
    private  final ItemImgRepository itemImgRepository;
    public void saveReview(ReviewFormDto reviewFormDto,  List<MultipartFile> itemImgFileList) throws Exception {
        Review review = Review.saveReview(reviewFormDto);
        reviewRepository.save(review);

        for (int i=0; i<itemImgFileList.size();i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setReview(review);
            if (i==0)
                itemImg.setReqImgYn("Y");
            else
                itemImg.setReqImgYn("N");
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
        }

    }

    public  List<ReviewFormDto> findByItemId(Long itemId){
        List<ReviewFormDto> reviewFormDtoList = new ArrayList<>();
        List<Review> review =  reviewRepository.findByItemId(itemId);
        for (Review re : review){
            List<ItemImg> itemImgList = itemImgRepository.findByReviewIdOrderByIdAsc(re.getId());
            List<ItemImgDto> itemImgDtoList = new ArrayList<>();

            for (ItemImg itemImg : itemImgList) {
                // Entity -> DTO
                ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
                itemImgDtoList.add(itemImgDto);
            }
            ReviewFormDto reviewFormDto = ReviewFormDto.of(re);
            reviewFormDto.setItemImgDtoList(itemImgDtoList);
            reviewFormDtoList.add(reviewFormDto);
        }

        return reviewFormDtoList;
    }

    public Optional<Review> findByMemberIdAndItemId(Long memberId, Long ItemId){
        return reviewRepository.findByMemberIdAndItemId(memberId, ItemId);
    }

    public  ReviewFormDto Dtoadd(Review review){
        List<ItemImg> itemImgList = itemImgRepository.findByReviewIdOrderByIdAsc(review.getId());
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            // Entity -> DTO
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        ReviewFormDto reviewFormDto = ReviewFormDto.of(review);
        reviewFormDto.setItemImgDtoList(itemImgDtoList);
        return reviewFormDto;


    }

}
