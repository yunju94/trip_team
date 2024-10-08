package com.trip.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.trip.constant.Category;
import com.trip.constant.Nature;
import com.trip.dto.*;
import com.trip.dto.QMainItemDto;

import com.trip.entity.*;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.trip.constant.Nature.DOMESTIC;
import static com.trip.constant.Nature.OVERSEAS;
import static com.trip.entity.QItem.item;
import static com.trip.entity.QItemImg.itemImg;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    private JPAQueryFactory queryFactory; // 동적쿼리 사용하기 위해 JPAQueryFactory 변수 선언
    // 생성자
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 실질적인 객체 생성
    }
    //여행지

    private BooleanExpression searchCategoryStatusEq( String category){
        Category str =null;
            if (category.equals("인천")){
                str= Category.INCHEON;
            }else if (category.equals("서울")) {
                str= Category.SEOUL;
            }else if (category.equals("대전")) {
                str=Category.DAEJEON;
            }else if (category.equals("양양")) {
                str=Category.YANGYANG;
            }else if (category.equals("부산")) {
                str= Category.BUSAN;
            }else if (category.equals("제주도")) {
                str = Category.JEJU;
            }else if (category.equals("미국")) {
                str = Category.USA;
            } else if (category.equals("필리핀")) {
                str = Category.PHILIPPINES;
            } else if (category.equals("베트남")) {
                str = Category.NHA_TRANG;
            } else if (category.equals("코타키나발루")) {
                str = Category.KOTA_KINABALU;
            } else if (category.equals("일본")) {
                str=Category.JAPAN;
            } else if (category.equals("하와이")) {
                str = Category.HAWAII;
            } else {
                str = null;
            }


        return item.category.eq(str);
    }



    //출발지
    //startIncheon,startBusan,startDeagu,startChungju,startGwangju,startYangyang,startJeju
    // 날짜              08/06/2024%20-%2008/09/2024 ===>07/15/2024 - 07/18/2024
    private BooleanExpression DateChangeStartDate(String StartPlace) {
        String Date = StartPlace;
        if (Date.length() == 0 ){
            return null;
        }
        String startDate = Date.substring(0, 10);//07/15/2024
        String[] str = startDate.split("/");
        String Start =  str[2]+"-"+ str[0]+"-"+ str[1]; //2024-07-15
        String endDate = Date.substring(13, 23); //07/18/2024
        str = endDate.split("/");
        String End = str[2]+"-" + str[0]+"-"+ str[1]; //2024-07-18
        return item.startDate.eq(LocalDate.parse(Start));

    }



    @Override
    public Page<MainItemDto> getAdminItemPage(ItemSearchDto itemSearchDto,  Pageable pageable){
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id,item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price,item.nature,item.itemSellStatus,item.startDate,item.endDate))
                .from(itemImg)
                .join(itemImg.item, item)
                .where(DateChangeStartDate(itemSearchDto.getDatefilter()),//출발일(itemSearchDto.getDatefilter())
                searchCategoryStatusEq(itemSearchDto.getPlaceSearch()))
                       //여행지(itemSearchDto.getPlaceSearch()로 국내 국외 찾기)
                .orderBy(item.id.desc())//id가 내림차순순으로
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }


    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QMainItemDto @QueryProjection을 활용하면 Dto 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id,item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price,item.nature,item.itemSellStatus,item.startDate,item.endDate))
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.reqImgYn.eq("Y"))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }



    private BooleanExpression searchStatus(String search){

        return item.itemNm.like("%"+ search + "%");
    }

    private BooleanExpression searchDetailStatus( String search){

        return item.itemDetail.like("%"+ search + "%");
    }
    @Override
    public Page<MainItemDto> searchItemPage(Pageable pageable,String search) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(new  QMainItemDto(item.id,item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price,item.nature,item.itemSellStatus, item.startDate,item.endDate))
                .from(itemImg).join(itemImg.item, item)
                .where(itemImg.reqImgYn.eq("Y"))
                .where(searchStatus(search))
                .where(searchDetailStatus(search))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    public  Nature viewNature(List<Viewer> viewerList){
        int domestic = 0;
        int overseas = 0;
        for (Viewer view : viewerList){
            if (view.getItem().getNature().equals(DOMESTIC)){
                domestic+= 1;
            }else {
                overseas+=1;
            }
        }
        return domestic >= overseas ? DOMESTIC : OVERSEAS;

    }
    public Page<MainItemDto> MemberItemPage(Pageable pageable, List<Viewer> viewerList) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(new  QMainItemDto(item.id,item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price,item.nature,item.itemSellStatus,item.startDate,item.endDate))
                .from(itemImg).join(itemImg.item, item)
                .where(itemImg.reqImgYn.eq("Y"))
                .where(item.nature.eq(viewNature(viewerList)))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


    public  BooleanExpression natureCategory(String nature){
        if ("domestic".equals(nature)){
          return   item.nature.eq(DOMESTIC);
        }else {
            System.out.println("oooooooooooooooooooooooooooo");
          return   item.nature.eq(OVERSEAS);
        }
    }

    @Override//나라별로 item 나누기
    public Page<MainItemDto> natureItemPage(Pageable pageable, String nature) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(new  QMainItemDto(item.id,item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price,item.nature,item.itemSellStatus,item.startDate,item.endDate))
                .from(itemImg).join(itemImg.item, item)
                .where(itemImg.reqImgYn.eq("Y"))
                .where(natureCategory(nature))// 나라가 정해진 걸로 나뉨
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }


}










