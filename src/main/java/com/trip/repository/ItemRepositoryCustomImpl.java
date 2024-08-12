package com.trip.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.trip.constant.Category;
import com.trip.dto.*;
import com.trip.dto.QMainItemDto;

import com.trip.entity.Item;
import com.trip.entity.QItem;
import com.trip.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;

import static com.trip.constant.Nature.DOMESTIC;
import static com.trip.constant.Nature.OVERSEAS;

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


        return QItem.item.category.eq(str);
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
        return QItem.item.startDate.eq(LocalDate.parse(Start));

    }



    @Override
    public List<Item> getAdminItemPage(ItemSearchDto itemSearchDto, int offset, int limit){
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item).//item에서 찾는다.
                where(DateChangeStartDate(itemSearchDto.getDatefilter()),//출발일(itemSearchDto.getDatefilter())
                searchCategoryStatusEq(itemSearchDto.getPlaceSearch()))
                       //여행지(itemSearchDto.getPlaceSearch()로 국내 국외 찾기)
                .orderBy(QItem.item.id.desc())//id가 내림차순순으로
                .offset(offset).limit(limit).fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return content;
    }


    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QMainItemDto @QueryProjection을 활용하면 Dto 바로 조회 가능
        QueryResults<MainItemDto> results = queryFactory.select(new QMainItemDto(item.id,item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price,item.nature,item.startDate,item.endDate))
                .from(itemImg).join(itemImg.item, item).where(itemImg.reqImgYn.eq("Y"))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }



    private BooleanExpression searchStatus( String search){

        return QItem.item.itemNm.like("%"+ search + "%");
    }

    private BooleanExpression searchDetailStatus( String search){

        return QItem.item.itemDetail.like("%"+ search + "%");
    }

    public Page<MainItemDto> searchItemPage(Pageable pageable, String search) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(new  QMainItemDto(item.id,item.itemNm,
                        item.itemDetail,itemImg.imgUrl,item.price,item.nature,item.startDate,item.endDate))
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

}










