package com.trip.controller;

import com.trip.constant.ItemSellStatus;
import com.trip.dto.ItemFormDto;
import com.trip.dto.ItemSearchDto;
import com.trip.dto.MainItemDto;
import com.trip.dto.ReviewFormDto;
import com.trip.entity.Item;
import com.trip.entity.Member;
import com.trip.entity.Review;
import com.trip.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.IsolationLevelDataSourceAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final MemberService memberService;
    private  final ViewService viewService;
    private  final ItemImgService itemImgService;
    private  final ReviewService reviewService;
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
//    @GetMapping(value = {"/items", "/items/page"})
//    public String itemManage(ItemSearchDto itemSearchDto,
//                             Model model){
//        // page.isPresent() -> page 값이 있는지 확인
//        // 값 있을 시 page.get() , 값 없을 시 0
//        // 한 페이지에 개수 -> 5개
//
//        model.addAttribute("itemSearchDto",itemSearchDto);
//
//
//        int count = 0;
//        model.addAttribute("count", count);
//
//        if (itemSearchDto.getPlaceSearch().equals("인천")|| itemSearchDto.getPlaceSearch().equals("서울")||
//                itemSearchDto.getPlaceSearch().equals("부산")|| itemSearchDto.getPlaceSearch().equals("양양")||
//                itemSearchDto.getPlaceSearch().equals("대전")|| itemSearchDto.getPlaceSearch().equals("제주도")){
//            //itemsearchDto에서 국내 여행지일 경우 국내 여행 사이트로 연결
//            return "nature/domestic";
////아닐 경우 해외 여행 사이트로 연결
//        }else {
//
//            return "nature/overseas";
//        }
//
//    }




    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId")Long itemId, Principal principal){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDto);


        Member member =memberService.memberload(principal.getName());
        model.addAttribute("member", member);

        List<ReviewFormDto> reviewList =  reviewService.findByItemId(itemId);
        model.addAttribute("reviewList", reviewList);

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

    @DeleteMapping(value = "/admin/item/delete/{Id}")
    public @ResponseBody ResponseEntity deleteItemDtl(@PathVariable Long Id, Model model){

        try {
            viewService.deleteItem(Id);
            itemImgService.deleteImg(Id);
            itemService.deleteItemId(Id);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "삭제 중 오류 발생!");
        }
        String jsonResponse = "{\"Id\": \"" + Id + "\"}";
        return  new ResponseEntity<>(jsonResponse, HttpStatus.OK);

    }

    @PostMapping(value = "/item/count/{Itemcount}/{itemId}")
    public @ResponseBody ResponseEntity itemCountSave (@PathVariable(name = "Itemcount") int Itemcount,
                                                       @PathVariable(name = "itemId") Long itemId,
                                                       Principal principal){

        String email=principal.getName();
        Member member = memberService.memberload(email);
        viewService.savememberItem(member, itemId);


        int ItemCount=itemService.countPlus(Itemcount, itemId);
        String jsonResponse = "{\"count\": \"" + ItemCount + "\"}";


        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }
//    @GetMapping(value = {"/items/searchText/{search}", "/items/searchText/{search}/{page}"})
//    public  String searchItem (@PathVariable String search, Model model,@PathVariable("page") Optional<Integer> page){
//
//        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
//
//        Page<MainItemDto> items = itemService.searchItemPage(pageable, search);
//
//        model.addAttribute("items", items);
//        model.addAttribute("search", search);
//        model.addAttribute("maxPage", 5);
//
//
//        return "nature/SearchItem";
//    }

    @PostMapping(value = {"/item/scrolling", "/item/scrolling/{page}/{search}/{str}"})
    public @ResponseBody ResponseEntity<Map<String, Object>> searchItemScroll(
            @PathVariable(name = "page", required = false) Integer page,
            @PathVariable(name = "search", required = false) String search,
            @PathVariable(name = "str", required = false) String str) {

        // page가 null인 경우 기본값 설정
        if (page == null) {
            page = 0; // 첫 페이지
        }
        Pageable pageable = PageRequest.of(page, 5);
        Page<MainItemDto> items;

        if (str != null && !str.isEmpty()) {
            items = itemService.natureItemPage(pageable, str);
        } else if (search != null && !search.isEmpty()) {
            items = itemService.searchItemPage(pageable, search);
        } else {
            items = Page.empty();
        }

        // 상품 상태 변경 로직
        LocalDate currency = LocalDate.now();
        for (MainItemDto mainItemDto : items) {
            if (mainItemDto.getStartDate().isBefore(currency) &&
                    mainItemDto.getItemSellStatus().equals(ItemSellStatus.SELL)) {
                itemService.ItemSellStatusChange(mainItemDto.getId());
            }
        }

        // JSON 응답 준비
        Map<String, Object> response = new HashMap<>();
        response.put("items", items.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @GetMapping(value = {"/items/{str}", "/items/{str}/{page}",
                        "/items/searchText/{search}", "/items/searchText/{search}/{page}",
                            "items/page"})
    public  String NatureItem (@PathVariable(required = false) String str,
                               @PathVariable(required = false) String search,
                               @PathVariable(required = false) Integer page,
                               @RequestParam(required = false) String placeSearch,
                               @RequestParam(required = false) String startPlace,
                               @RequestParam(required = false) String datefilter,Model model){


        // ItemSearchDto 초기화
        ItemSearchDto itemSearchDto = new ItemSearchDto(); // 초기화

        Pageable pageable = PageRequest.of(page != null ? page : 0, 5);
        Page<MainItemDto> items;


        if (!placeSearch.isEmpty()){
            itemSearchDto.setPlaceSearch(placeSearch);
            System.out.println(placeSearch);
        }
        if (!startPlace.isEmpty()){
            itemSearchDto.setStartPlace(startPlace);
            System.out.println(startPlace);
        }
        if (!datefilter.isEmpty()){
            itemSearchDto.setDatefilter(datefilter);
            System.out.println(datefilter);
        }

        // 검색용
        if (str != null) {
            if ("domestic".equals(str)) {
                items = itemService.natureItemPage(pageable, str);
                System.out.println("11111111111111111111111111111111");

            } else if ("overseas".equals(str)) {
                items = itemService.natureItemPage(pageable, str);
                System.out.println("2222222222222222222222");
            } else {
                items = itemService.getMainItemPage(itemSearchDto, pageable); // Default handling
                System.out.println("333333333333333333333333");
            }
            model.addAttribute("str", str);
        } else {
            if (search != null) {
                items = itemService.searchItemPage(pageable, search);
                model.addAttribute("search", search);
                System.out.println("4444444444444444444444444444444");
            } else {
                items = itemService.getAdminItemPage(itemSearchDto, pageable); // Default handling
                System.out.println("555555555555555555555555555555");
            }
        };

        LocalDate currency = LocalDate.now();
        for (MainItemDto mainItemDto : items) {
            if (mainItemDto.getStartDate().isBefore(currency)) {
                if (mainItemDto.getItemSellStatus().equals(ItemSellStatus.SELL)) {
                    itemService.ItemSellStatusChange(mainItemDto.getId());
                }
            }

            System.out.println(mainItemDto.getItemNm());
        }

        model.addAttribute("items", items);
        model.addAttribute("maxPage", 5);

        return "nature/nature";
    }







}