package com.trip.controller;

import com.trip.constant.Nature;
import com.trip.dto.ItemDto;
import com.trip.entity.Item;
import com.trip.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NatureController {
    @GetMapping(value ="/domestic")
    public String doList() {
        return "nature/domestic";
    }
    @GetMapping(value = "/overseas")
    public String ovList() {
        return "nature/overseas";
    }
}
