package com.trip.controller;


import com.trip.service.MailService;
import com.trip.service.MemberService;
import com.trip.dto.MemberFormDto;
import com.trip.entity.Member;
import com.trip.service.TestCoolSms;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;


    String confirm = "";
    boolean confirmCheck = false;

    @GetMapping(value = "/login")
    public String loginMember(){
        return  "member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto",new MemberFormDto());
        return "member/memberForm";
    }
    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                             Model model) {

        // @Valid 붙은 객체를 검사해서 결과에 에러가 있으면 실행
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult);
            return "member/memberForm";//다시 회원가입으로 돌려보닙니다.
        }
        try{
            //Member 객체 생성
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            //데이터베이스에 저장
            memberService.saveMember(member);
        }
        catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }
        return "member/anno";
    }

    @PostMapping("/{email}/emailConfirm")
    public @ResponseBody ResponseEntity emailConfrim(@PathVariable("email") String email)
            throws Exception{
        System.out.println("email");

        confirm = mailService.sendSimpleMessage(email);
        System.out.println("인증코드 : "+ confirm);
        return new ResponseEntity<String>("인증 메일을 보냈습니다.", HttpStatus.OK);
    }
    @PostMapping("/{code}/codeCheck")
    public @ResponseBody ResponseEntity codeConfirm(@PathVariable("code")String code)
            throws Exception {
        if (confirm.equals(code)) {
            confirmCheck = true;
            return new ResponseEntity<String>("인증 성공하였습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증 코드를 올바르게 입력해주세요.", HttpStatus.BAD_REQUEST);
    }
}
