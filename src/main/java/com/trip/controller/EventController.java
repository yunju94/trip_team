package com.trip.controller;

import com.trip.constant.Role;
import com.trip.dto.EventFormDto;
import com.trip.dto.ItemFormDto;
import com.trip.entity.Member;
import com.trip.entity.event;
import com.trip.service.EventService;
import com.trip.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EventController {

    private  final EventService eventService;
    private final MemberService memberService;
    @GetMapping(value = "/event")
    public String eventMain(Model model){
        List<event> events=eventService.AllSearch();
        model.addAttribute("event", events);
        return "event/EventMain";
    }

    @GetMapping(value = "/event/new")
    public String itemForm(Model model){
        model.addAttribute("EventFormDto",new EventFormDto());
        return "event/FormWrite";
    }

    @PostMapping(value = "/event/new/write")
    public String itemFormWrite(@Valid EventFormDto eventFormDto, BindingResult bindingResult,
                                @RequestParam("eventImgFile") List<MultipartFile> eventImgFileList , Model model){

        if (bindingResult.hasErrors()) {
            return "event/FormWrite";
        }
        try {

            eventService.saveEventTem(eventFormDto, eventImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "상품 등록 중 에러가 발생하였습니다.");
            return "event/FormWrite";
        }
        return "redirect:/";
    }

    @GetMapping(value ="/event/{id}")
    public  String eventIdSearch(@PathVariable("id") Long eventId, Model model, Principal principal){

        if (principal.getName() == null){
            return "member/memberLoginForm";
        }

        Optional<event> eventOptional  = eventService.seachEvent(eventId);
        model.addAttribute("event", eventOptional.orElse(null));



        Member member = memberService.memberload(principal.getName());
        if (member == null){
            member=new Member();
            member.setRole(Role.USER);

        }
        model.addAttribute("member", member);
        return "event/EventDtl";
    }
    @GetMapping(value ="/event/update/{id}")
    public  String eventIdupdate(@PathVariable("id") Long eventId, Model model){

        try{
            EventFormDto eventFormDto = eventService.geteventIdDtl(eventId);
            model.addAttribute("EventFormDto", eventFormDto);

        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("eventFormDto", new EventFormDto());
            return  "event/FormWrite";
        }

        return "event/FormWrite";
    }


    @PostMapping("/event/update/{eventId}")
    public String itemUpdate(@Valid  EventFormDto eventFormDto,
                             @RequestParam("eventImgFile") List<MultipartFile> eventImgFile,
                             BindingResult bindingResult,
                             Model model,
                             @PathVariable("eventId") Long eventId){

        if (bindingResult.hasErrors()) {
            return "event/FormWrite";
        }
        try {
            eventService.updateItem(eventFormDto, eventImgFile);
        } catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "event/FormWrite";
        }

        return "redirect:/";
    }

    @GetMapping(value ="/event/delete/{id}")
    public  String eventIddelete(@PathVariable("id") Long eventId){
        eventService.getEventDelete(eventId);
        return "event/EventMain";
    }



}
