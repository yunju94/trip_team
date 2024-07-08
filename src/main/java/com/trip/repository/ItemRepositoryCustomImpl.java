package com.trip.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.trip.dto.ItemSearchDto;
import com.trip.dto.MainItemDto;
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
    private BooleanExpression searchNatureStatusEq(String nature){
        if (nature.equals("placeIncheon")||nature.equals("placeSeoul")||
                nature.equals("placeDeajeon")||nature.equals("placeYangyang")||
                nature.equals("placeBusan")||nature.equals("placeJeju")){
            nature= "DOMESTIC";}
        return nature.equals("DOMESTIC")?
                QItem.item.nature.eq(DOMESTIC): QItem.item.nature.eq(OVERSEAS);

    }
    //여행지가 placeIncheon,placeSeoul, placeDeajeon, placeYangyang, placeBusan, placeJeju 이면,
    //국내 DOMESTIC
    //여행지가 placeAme, placePhi, placeVie,placeKot,placeJap, placeHaw 이면,
    //해외 OVERSEAS


    //출발지
    //startIncheon,startBusan,startDeagu,startChungju,startGwangju,startYangyang,startJeju
    // 날짜              08/06/2024%20-%2008/09/2024
    private BooleanExpression DateChange(String StartPlace) {
        String Date = StartPlace;
        String startDate = Date.substring(10);//08/06/2024
        String[] str = startDate.split("/");
        String Start = str[2] +"-" + str[0]+"-" + str[1]; //2024-08-06

        String endDate = Date.substring(17, 27); //08/09/2024
        str = endDate.split("/");
        String End = str[2] +"-" + str[0]+"-" + str[1]; //2024-08-09

        return QItem.item.startDate.eq(LocalDate.parse(Start));

    }


    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QueryResults<Item> results = queryFactory.selectFrom(QItem.item).
                where(DateChange(itemSearchDto.getDatefilter()),//출발일
                        searchNatureStatusEq(itemSearchDto.getPlaceSearch()))//여행지
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
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
}










