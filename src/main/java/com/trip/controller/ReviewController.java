package com.trip.controller;

import com.trip.dto.ReviewFormDto;
import com.trip.entity.Item;
import com.trip.entity.Member;
import com.trip.entity.Order;
import com.trip.entity.Review;
import com.trip.service.ItemService;
import com.trip.service.MemberService;
import com.trip.service.OrderService;
import com.trip.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private  final ReviewService reviewService;
    private  final MemberService memberService;
    private  final ItemService itemService;
    private  final OrderService orderService;
    @GetMapping(value = "/review/new/{itemNm}")
    public String reviewNew (Model model, Principal principal, @PathVariable String itemNm){
        Member member = memberService.memberload(principal.getName());
        Item item = itemService.searchItemNm(itemNm);

        Optional<Review> review = reviewService.findByMemberIdAndItemId(member.getId(), item.getId());
        if (review.isPresent()){
            ReviewFormDto reviewFormDto = reviewService.Dtoadd(review.get());
            model.addAttribute("reviewFormDto", reviewFormDto);
        }else {
            ReviewFormDto reviewFormDto = new ReviewFormDto();
            reviewFormDto.setMember(member);
            reviewFormDto.setItem(item);
            model.addAttribute("reviewFormDto", reviewFormDto);
        }

        return "mypage/review/Form";
    }

    @PostMapping(value = "/review/Formsave")
    public String reviewNew(@Valid ReviewFormDto reviewFormDto ,
                            BindingResult bindingResult,
                            Model model,
                            @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList)
            throws Exception {
        if (bindingResult.hasErrors()) {
            return "mypage/review/Form";
        }

        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage",
                    "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "mypage/review/Form";
        }
        try{
            reviewService.saveReview(reviewFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage",
                    "등록 중 오류 발생!");
        }


        return "redirect:/";
    }
}
