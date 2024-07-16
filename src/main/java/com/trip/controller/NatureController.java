package com.trip.controller;

import com.trip.constant.Nature;
import com.trip.dto.ItemDto;

import com.trip.entity.Item;

import com.trip.service.ItemService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;

import java.util.List;



@Controller
@RequiredArgsConstructor
public class NatureController {

    private final ItemService itemService;

    @GetMapping(value ="/domestic")
    public String doList(ItemDto itemDto, Model model) {
        List<Item> itemList = itemService.getItemAll();
        model.addAttribute("items", itemList);
        return "nature/domestic";
    }

    @GetMapping(value ="/overseas")
    public String ovList(ItemDto itemDto, Model model) {
        List<Item> itemList = itemService.getItemAll();
        model.addAttribute("items", itemList);
        return "nature/overseas";
    }
    @PostMapping("/domestic")
    public ResponseEntity<String> getDomesticData(
            @RequestParam int nextPageLimit,
            @RequestParam int limit,
            @RequestParam(required = false) List<Long> loadedItemIds // 변경된 부분
    ) {
        if (loadedItemIds == null) {
            loadedItemIds = new ArrayList<>(); // 기본 값 설정
        }

        List<Item> items = itemService.getItems(nextPageLimit, limit);
        StringBuilder sb = new StringBuilder();

        for (Item item : items) {
            if (!loadedItemIds.contains(item.getId()) && item.getNature() == Nature.DOMESTIC) {
                sb.append("<tr data-item-id=\"").append(item.getId()).append("\">")
                        .append("<td>")
                        .append("<div class='image-card'>")
                        .append("<div sec:authorize='hasRole(\"ADMIN\")'>")
                        .append("<input type='checkbox' id='item_").append(item.getId()).append("' name='selectedItems' value='").append(item.getId()).append("' class='item-checkbox'>")
                        .append("</div>")
                        .append("<a href='/item/").append(item.getId()).append("' class='text-dark'>")
                        .append("<img src='").append(item.getItemImgs().get(0).getImgUrl()).append("' alt='").append(item.getItemNm()).append("'>")
                        .append("</a>")
                        .append("<div class='item-details'>")
                        .append("<div class='item-name'>").append(item.getItemNm()).append("</div>")
                        .append("<div class='item-price'>").append(item.getPrice()).append("원</div>")
                        .append("<div class='item-category'>출발지 : ").append(item.getCategory().getDisplayName()).append("</div>")
                        .append("</div>")
                        .append("</div>")
                        .append("</td>")
                        .append("</tr>");
            }
        }

        return ResponseEntity.ok(sb.toString());
    }
    @PostMapping("/overseas")
    public ResponseEntity<String> getOverseasData(
            @RequestParam int nextPageLimit,
            @RequestParam int limit,
            @RequestParam(required = false) List<Long> loadedItemIds
    ) {
        if (loadedItemIds == null) {
            loadedItemIds = new ArrayList<>(); //
        }

        List<Item> items = itemService.getItems(nextPageLimit, limit);
        StringBuilder sb = new StringBuilder();

        for (Item item : items) {
            if (!loadedItemIds.contains(item.getId()) && item.getNature() == Nature.OVERSEAS) {
                sb.append("<tr data-item-id=\"").append(item.getId()).append("\">")
                        .append("<td>")
                        .append("<div class='image-card'>")
                        .append("<div sec:authorize='hasRole(\"ADMIN\")'>")
                        .append("<input type='checkbox' id='item_").append(item.getId()).append("' name='selectedItems' value='").append(item.getId()).append("' class='item-checkbox'>")
                        .append("</div>")
                        .append("<a href='/item/").append(item.getId()).append("' class='text-dark'>")
                        .append("<img src='").append(item.getItemImgs().get(0).getImgUrl()).append("' alt='").append(item.getItemNm()).append("'>")
                        .append("</a>")
                        .append("<div class='item-details'>")
                        .append("<div class='item-name'>").append(item.getItemNm()).append("</div>")
                        .append("<div class='item-price'>").append(item.getPrice()).append("원</div>")
                        .append("<div class='item-category'>출발지 : ").append(item.getCategory().getDisplayName()).append("</div>")
                        .append("</div>")
                        .append("</div>")
                        .append("</td>")
                        .append("</tr>");
            }
        }

        return ResponseEntity.ok(sb.toString());
    }
}