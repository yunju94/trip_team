package com.trip.controller;

import com.trip.dto.ItemDto;
import com.trip.dto.MainItemDto;
import com.trip.entity.Item;

import com.trip.service.ItemService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.trip.constant.Nature.DOMESTIC;
import static com.trip.constant.Nature.valueOf;

@Controller
@RequiredArgsConstructor
public class NatureController {


    private final ItemService itemService;
    @GetMapping(value ="/domestic")
    public String doList(ItemDto itemDto, Model model) {
        List<Item> itemList = itemService.getItemAll();
        model.addAttribute("items", itemList);
r
        return "nature/domestic";

    }

    @GetMapping(value ="/overseas")
    public String ovList(ItemDto itemDto, Model model) {
        List<Item> itemList = itemService.getItemAll();
        model.addAttribute("items", itemList);
        return "nature/overseas";
    }
}
