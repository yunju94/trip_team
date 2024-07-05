package com.trip.dto;


import com.trip.constant.ItemSellStatus;
import com.trip.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

public class ItemFormDto {
    //Item
    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다")
    private String itemDetail;

    @NotBlank(message = "출발일은 필수 입력 값입니다")
    private String departuredate;

    @NotBlank(message = "도착일은 필수 입력 값입니다")
    private String arrivaldate;

    private ItemSellStatus itemSellStatus;

    //-----------------------------------ItemImage

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품 이미지 정보
    private List<Long> itemImgIds = new ArrayList<>(); // 상품 이미지 아이디

    //------------------------------------ ModelMapper

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        // ItemFormDto -> item 연결
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        // item -> ItemFormDto 연결
        return modelMapper.map(item, ItemFormDto.class);
    }
}

