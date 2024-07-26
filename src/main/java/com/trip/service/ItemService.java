package com.trip.service;


import com.trip.constant.Nature;
import com.trip.dto.*;
import com.trip.entity.*;
import com.trip.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.trip.constant.Nature.DOMESTIC;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private  final ItemImgRepository itemImgRepository;
    private final OrderItemRepository orderItemRepository;
    private final  MemberRepository memberRepository;
    private  final OrderRepository orderRepository;



    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception{
        //상품등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        //이미지 등록
        for (int i=0; i<itemImgFileList.size();i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i==0)
                itemImg.setReqImgYn("Y");
            else
                itemImg.setReqImgYn("N");
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        //Entity
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        //DB에서 데이터를 가지고 온다.
        //DTO
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();

        for (ItemImg itemImg : itemImgList) {
            // Entity -> DTO
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        // Item -> ItemFormDto modelMapper
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList)
            throws Exception{
        //상품 변경
        Item item = itemRepository.findById(itemFormDto.getId()).
                orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        //상품 이미지 변경
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for (int i=0; i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true) // 쿼리문 실행  읽기만 가능
    public List<Item> getAdminItemPage(ItemSearchDto itemSearchDto, int nextPageLimit, int limit){
        int offset = nextPageLimit - limit;
        return itemRepository.getAdminItemPage(itemSearchDto,offset, limit);
    }
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return  itemRepository.getMainItemPage(itemSearchDto,pageable);
    }

    public List<Item> getItemAll(){
        return itemRepository.findAll();
    }

    public  OrderItem itemName(String itemNm){

        List<Item> item = itemRepository.findByItemNm(itemNm);
        System.out.println(item.getFirst().getId());


        OrderItem orderItem = orderItemRepository.findByItemId(item.getFirst().getId());

        return orderItem;


    }
    public void deleteItems(List<Long> itemIds) {
        for (Long itemId : itemIds) {
            itemRepository.deleteById(itemId);
        }
    }
    public List<Item> getItems(int nextPageLimit, int limit) {
        int offset = nextPageLimit - limit;
        return itemRepository.findItemsWithLimit(offset, limit);
    }
    public List<Item> getOverseasItems(int nextPageLimit, int limit) {
        int offset = nextPageLimit - limit;
        return itemRepository.findItemsByNature(Nature.OVERSEAS);
    }

    public  int countPlus(int count, long itemId){
        Item item =itemRepository.findById(itemId).orElseThrow();
        item.setCount(count+1);
        itemRepository.save(item);
        return item.getCount();
    }
}
