package com.trip.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.trip.constant.Category;
import com.trip.constant.Region;
import com.trip.dto.EventDto;
import com.trip.dto.MainItemDto;
import com.trip.dto.QEventDto;
import com.trip.dto.QMainItemDto;
import com.trip.entity.QItem;
import com.trip.entity.QItemImg;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public class EventRepositoryCustomImpl implements EventRepositoryCustom{

    private JPAQueryFactory queryFactory; // 동적쿼리 사용하기 위해 JPAQueryFactory 변수 선언
    // 생성자
    public EventRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 실질적인 객체 생성
    }


    public BooleanExpression EvnetSearchRegion(String search){
        return QItem.item.category.eq(Category.valueOf(search));
    }
    @Override
    public Page<EventDto> getEventJapan(Pageable pageable, String search) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QMainItemDto @QueryProjection을 활용하면 Dto 바로 조회 가능
        QueryResults<EventDto> results = queryFactory.select(new QEventDto(item.id, item.itemNm,
                        itemImg.imgUrl,  item.itemDetail, item.price))

                .from(itemImg).
                join(itemImg.item, item)
                .where(itemImg.reqImgYn.eq("Y"))
                .where(EvnetSearchRegion(search))

                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<EventDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }



    @Override
    public Page<EventDto> getEventDate(Pageable pageable, LocalDate startDate, LocalDate endDate) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        //QMainItemDto @QueryProjection을 활용하면 Dto 바로 조회 가능
        QueryResults<EventDto> results = queryFactory.select(new QEventDto(item.id, item.itemNm,
                        itemImg.imgUrl,  item.itemDetail, item.price))
                .from(itemImg).
                join(itemImg.item, item)
                .where(itemImg.reqImgYn.eq("Y"))
                .where(item.startDate.between(startDate, endDate))
                .orderBy(item.id.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetchResults();
        List<EventDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content,pageable,total);
    }
}
