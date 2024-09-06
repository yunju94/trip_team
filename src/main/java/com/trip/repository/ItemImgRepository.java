package com.trip.repository;

import com.trip.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
    ItemImg findByItemIdAndReqImgYn(Long itemId, String reqImgYn);


    List<ItemImg> findByReviewIdOrderByIdAsc (Long ReviewId);
}
