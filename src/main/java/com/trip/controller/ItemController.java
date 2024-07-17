package com.trip.controller;

import com.trip.dto.ItemFormDto;
import com.trip.dto.ItemSearchDto;
import com.trip.entity.Item;
import com.trip.entity.Member;
import com.trip.service.ItemService;
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
public class ItemController {
    private final ItemService itemService;
    private final MemberService memberService;
    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }
    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {

        if (bindingResult.hasErrors()) {

            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage",
                    "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            System.out.println(itemFormDto.getStartDate());
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage",
                    "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model){
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto",new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String  itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                              @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                              Model model){
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty()&&itemFormDto.getId()==null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    // value 2개인 이유 -> 1. 네비게이션에서 상품관리 클릭 2. 상품관리 안에서 페이지 이동
    @GetMapping(value = {"/admin/items", "/admin/items/page"})
    public String itemManage(ItemSearchDto itemSearchDto, Optional<Integer> page,
                             Model model){
        // page.isPresent() -> page 값이 있는지 확인
        // 값 있을 시 page.get() , 값 없을 시 0
        // 한 페이지에 개수 -> 5개

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,5);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto,pageable);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",5);
        System.out.println(itemSearchDto.getPlaceSearch());
        if (itemSearchDto.getPlaceSearch().equals("인천")|| itemSearchDto.getPlaceSearch().equals("서울")||
                itemSearchDto.getPlaceSearch().equals("부산")|| itemSearchDto.getPlaceSearch().equals("양양")||
                itemSearchDto.getPlaceSearch().equals("대전")|| itemSearchDto.getPlaceSearch().equals("제주도")){
            //itemsearchDto에서 국내 여행지일 경우 국내 여행 사이트로 연결
            return "nature/domestic";
//아닐 경우 해외 여행 사이트로 연결
        }else {
            return "nature/overseas";
        }



    }
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId")Long itemId, Principal principal){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDto);

        Member member =memberService.memberload(principal.getName());
        model.addAttribute("member", member);
        return "item/itemDtl";
    }
    @PostMapping("/admin/items/delete")
    public String deleteItems(@RequestParam(name = "selectedItems", required = false) List<Long> selectedItems) {
        if (selectedItems != null && !selectedItems.isEmpty()) {
            try {
                itemService.deleteItems(selectedItems);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/";
    }


}