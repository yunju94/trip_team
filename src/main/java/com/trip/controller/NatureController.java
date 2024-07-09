package com.trip.controller;

import com.trip.constant.Nature;
import com.trip.dto.ItemDto;
import com.trip.entity.Item;
import com.trip.repository.ItemRepository;
import com.trip.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.trip.constant.Nature.DOMESTIC;
import static com.trip.constant.Nature.valueOf;

@Controller
@RequiredArgsConstructor
public class NatureController {
    private  final ItemService itemService;
    @GetMapping(value ="/domestic")
    public String doList( ItemDto itemDto,Model model) {
        List<Item> itemList = itemService.getItemAll();
        model.addAttribute("items", itemList);


        return "nature/domestic";

    }
    @GetMapping(value = "/overseas")
    public String ovList() {
        return "nature/overseas";
    }
}
