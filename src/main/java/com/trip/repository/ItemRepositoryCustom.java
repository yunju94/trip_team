package com.trip.repository;

import com.trip.dto.CartViewDto;
import com.trip.dto.ItemSearchDto;
import com.trip.dto.MainItemDto;
import com.trip.entity.Item;
import com.trip.entity.Member;
import com.trip.entity.Viewer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> getAdminItemPage(ItemSearchDto itemSearchDto, int offset, int  limit);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> searchItemPage(Pageable pageable, String search);
    Page<MainItemDto> MemberItemPage(Pageable pageable, List<Viewer> viewerList);

}
