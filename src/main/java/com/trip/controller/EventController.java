package com.trip.controller;

import com.trip.constant.Role;
import com.trip.dto.EventDto;
import com.trip.dto.EventFormDto;
import com.trip.dto.ItemFormDto;
import com.trip.dto.OrderHistDto;
import com.trip.entity.Member;
import com.trip.entity.event;
import com.trip.service.EventService;
import com.trip.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
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
            System.out.println(bindingResult);
            System.out.println(bindingResult.hasErrors());
            return "event/FormWrite";
        }
        try {
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            eventService.saveEventTem(eventFormDto, eventImgFileList);
            System.out.println("cccccccccccccccccccccccccccccccccccccccccccc");
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

    @GetMapping(value = {"/event/vetnam", "/event/vetnam/{page}"})
    public String Eventvetnam(@PathVariable Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 5);
        String search = "NHA_TRANG";
        Page<EventDto> eventDtoList = eventService.getEventJapan(pageable, search);

        model.addAttribute("eventDtoList", eventDtoList);
        model.addAttribute("maxPage", 5);



        return "event/vetnam";

    }
    @GetMapping(value = {"/event/dragon", "/event/dragon/{page}"})
    public String Eventgragon(@PathVariable Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 5);

        LocalDate startDate1 = LocalDate.of(2024,9,14);
        LocalDate endDate1 = LocalDate.of(2024,9,18);
        Page<EventDto> eventDtoList1 = eventService.getEventDate(pageable, startDate1, endDate1);
        if (LocalDate.now().isAfter(endDate1)){
            eventDtoList1 = null;
        }else {
            model.addAttribute("eventDtoList1", eventDtoList1);
        }

        LocalDate startDate2 = LocalDate.of(2024,10,3);
        LocalDate endDate2 = LocalDate.of(2024,10,9);
        Page<EventDto> eventDtoList2 = eventService.getEventDate(pageable, startDate2, endDate2);
        if (LocalDate.now().isAfter(endDate2)){
            eventDtoList2 = null;
        }else {
            model.addAttribute("eventDtoList2", eventDtoList2);
        }

        model.addAttribute("maxPage", 5);


        return "event/dragon";

    }

    @GetMapping(value = {"/event/japan","/event/japan/{page}" })
    public String Eventjapan(@PathVariable Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 5);
        String search = "JAPAN";
        Page<EventDto> eventDtoList = eventService.getEventJapan(pageable, search);

        model.addAttribute("eventDtoList", eventDtoList);
        model.addAttribute("maxPage", 5);

        return "event/japan";

    }
    @GetMapping(value = {"/event/america", "/event/america/{page}"})
    public String Eventamerica(@PathVariable Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 5);
        String search = "USA";
        Page<EventDto> eventDtoList = eventService.getEventJapan(pageable, search);

        model.addAttribute("eventDtoList", eventDtoList);
        model.addAttribute("maxPage", 5);

        return "event/america";

    }


}
