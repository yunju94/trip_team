package com.trip.dto;


import com.trip.constant.ItemSellStatus;
import com.trip.constant.Category;
import com.trip.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

public class ItemFormDto {
    private Long id;


    @NotBlank(message = "패키지명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상세설명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "마감인원은 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private Category Category;

    @NotNull(message = "출발날짜는 필수 입력 값입니다.")
    private LocalDate startDate;

    @NotNull(message = "도착날짜는 필수 입력 값입니다.")
    private LocalDate endDate;
    // -------------------------------------------------------
    // ItemImg
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 상품 이미지 정보

    private List<Long> itemImgIds = new ArrayList<>(); // 상품 이미지 아이디

    // -------------------------------------------------------
    // ModelMapper
    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem() {
        // ItemFormDto -> Item 연결
        return modelMapper.map(this,Item.class);
    }
    public static ItemFormDto of(Item item) {
        // Item -> ItemFormDto 연결
        return modelMapper.map(item, ItemFormDto.class);
    }
}



