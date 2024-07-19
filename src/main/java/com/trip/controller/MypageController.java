package com.trip.controller;

import com.trip.constant.Role;
import com.trip.dto.ItemFormDto;
import com.trip.dto.MemberFormDto;
import com.trip.dto.MileageDto;
import com.trip.dto.OrderHistDto;
import com.trip.entity.*;
import com.trip.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MypageController {
    private final OrderService orderService;
    private  final MemberService memberService;
    private  final MileageService mileageService;
    private  final QuestionsService questionsService;
    private  final CommentService commentService;

    @GetMapping(value = "/mypage")
    public String mypageOpen( Principal principal, Model model){
      //오더 아이디를 받는다. 개인정보를 받는다. 오더 아이디로 저장된 오더 정보를 불러온다.
        Optional<Order> order =  orderService.orderfind(principal.getName());
        if (order.isEmpty()){
            return "mypage/mypageOrder";
        }
        Optional<OrderItem> orderItem = orderService.orderItemDetail(order);
        //오더 중에서도 오더 아이템 정보를 불러온다.
        //해당 정보를 가지고 html로 간다
        model.addAttribute("orderItem", orderItem);
        return "mypage/mypageOrder";
    }

    @GetMapping(value = "/updateInfo")
    public  String updateInformation(Principal principal, Model model){
        Member member = memberService.memberload(principal.getName());
        model.addAttribute("memberFormDto", member);
        return "mypage/updateInfo";
    }
    @PostMapping(value = "/update/Info")
    public  String updateInformationPost(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "에러가 발생했습니다.");

        }
        try {
            memberService.updateMember(memberFormDto);
            model.addAttribute("errorMessage", "완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "수정 중 에러가 발생하였습니다.");


        }

        return "mypage/updateInfo";

    }


    @GetMapping(value = "/mileage")
    public String mileageOpen( Principal principal, Model model){
        //오더 아이디를 받는다. 개인정보를 받는다. 오더 아이디로 저장된 오더 정보를 불러온다.
        String email = principal.getName();
        Member member = memberService.memberload(email);
        if (member.getRole()==Role.USER){//멤버 정보가 유저일 경우
           List<Mileage> mileage =mileageService.membertoMileage(member.getId());
           model.addAttribute("mileage", mileage);
            return "mypage/userMileage";
        }
        return "mypage/AdminMileage";//멤버 정보가 관리자인 경우
    }
    @GetMapping("/Popup.asp")
    public String Popup(@RequestParam("memberId") Long memberId,
                        @RequestParam("memberName") String memberName,
                        Model model) {

        List<Member> member = memberService.findMemberSearch(memberId, memberName);

        model.addAttribute("memberlist", member);

        // 여기서 필요한 로직을 추가하여 모델에 데이터를 담아서 View로 전달할 수 있습니다.

        return "mypage/Popup"; // 실제 View의
    }

    @PostMapping(value = "/mileage/{memberId}/search")
    public @ResponseBody ResponseEntity mileageSearchMember(@PathVariable("memberId") Long id, Model model){
        String name= "";

        try{
            Optional<Member> member = memberService.findMember(id);

            name  = member.get().getName();

        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>(name, HttpStatus.OK);

    }
    @GetMapping(value = "/mileageOK/{memberId}/{content}/{point}")
    public String mileageSendUSER(@PathVariable("memberId") Long memberId,
                        @PathVariable("content") String content,
                        @PathVariable("point") int point) {
        MileageDto mileageDto = new MileageDto();
        mileageDto.setContent(content);
        mileageDto.setPoint(point);
        mileageService.mileageMembersend(mileageDto, memberId);
        // 여기서 필요한 로직을 추가하여 모델에 데이터를 담아서 View로 전달할 수 있습니다.
        return "redirect:/"; // 실제 View의
    }



    @GetMapping(value = "/userQuestion")
    public String userQuestion( Principal principal, Model model){
        //오더 아이디를 받는다. 개인정보를 받는다. 오더 아이디로 저장된 오더 정보를 불러온다.
        String email = principal.getName();
        Member member = memberService.memberload(email);

        List<Questions> questionsList = questionsService.userQuestionMember(member.getName());

        List<Comment> comments = new ArrayList<>();
        List<List<Comment>> commen = new ArrayList<>();
        for (int i = 0 ; i< questionsList.size(); i++){
            comments = commentService.getCommentsByQuestionId(questionsList.get(i).getId());
            commen.add(comments);

        }
        System.out.println(commen);
        System.out.println(questionsList);
        model.addAttribute("question", questionsList);
        model.addAttribute("comments", commen);
        return "mypage/UserQuestion";//멤버 정보가 관리자인 경우
    }
}
