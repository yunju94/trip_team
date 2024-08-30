package com.trip.controller;

import com.trip.constant.ItemSellStatus;
import com.trip.dto.ItemFormDto;
import com.trip.dto.ItemSearchDto;
import com.trip.dto.MainItemDto;
import com.trip.entity.Member;
import com.trip.service.ItemImgService;
import com.trip.service.ItemService;
import com.trip.service.MemberService;
import com.trip.service.ViewService;
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
            @PathVariable(name = "str", required = false) String str,
            @RequestParam(name = "page", defaultValue = "0") int pageParam) {

        // Default values if parameters are not provided
        page = (page != null) ? page : pageParam;
        Pageable pageable = PageRequest.of(page, 5);

        Page<MainItemDto> items;

        if (str != null && !str.isEmpty()) {
            items = itemService.natureItemPage(pageable, str);
            System.out.println(items);
            System.out.println("str: " + str);
        } else if (search != null && !search.isEmpty())  {
            items = itemService.searchItemPage(pageable, search);
            System.out.println("search: " + search);

        } else {
            items = Page.empty();
            System.out.println("No search or str parameter provided");
        }


        LocalDate currency=LocalDate.now();
        for (MainItemDto mainItemDto : items){
            if (mainItemDto.getStartDate().isBefore(currency)){
                if (mainItemDto.getItemSellStatus().equals(ItemSellStatus.SELL)){
                    itemService.ItemSellStatusChange(mainItemDto.getId());
                }
            }
        }

        // Prepare the JSON response
        Map<String, Object> response = new HashMap<>();
        response.put("items", items.getContent()); // Extract items list from Page object

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = {"/items/{str}", "/items/{str}/{page}",
                        "/items/searchText/{search}", "/items/searchText/{search}/{page}"})
    public  String NatureItem (@PathVariable Optional<String> str,
                               @PathVariable Optional<String> search,
                              @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<MainItemDto> items = null;
        if (str.isPresent()) {
            if ("domestic".equals(str.get())) {
                items = itemService.natureItemPage(pageable, str.get());
            } else if ("overseas".equals(str.get())) {
                items = itemService.natureItemPage(pageable, str.get());
            }
            model.addAttribute("str",str);
        } else {
            if (search.isPresent()) {
                items = itemService.searchItemPage(pageable, search.get());
                model.addAttribute("search", search.get());
            }
        }
        LocalDate currency=LocalDate.now();
        for (MainItemDto mainItemDto : items){
            if (mainItemDto.getStartDate().isBefore(currency)){
                if (mainItemDto.getItemSellStatus().equals(ItemSellStatus.SELL)){
                    itemService.ItemSellStatusChange(mainItemDto.getId());
                }
            }
        }
        model.addAttribute("items", items);
        model.addAttribute("maxPage", 5);


        return "nature/nature";
    }







}