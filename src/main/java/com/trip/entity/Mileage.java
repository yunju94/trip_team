package com.trip.entity;

import com.trip.dto.MileageDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Entity
@Table(name = "mileage")
@Getter
@Setter

public class Mileage {
    @Id
    @Column(name = "mileage_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private  String content;
    private  int point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Mileage() {
    }
    public static Mileage createMileage(MileageDto mileageDto, Member member){
        Mileage mileage = new Mileage();
        mileage.id= mileageDto.getId();
        mileage.content = mileageDto.getContent();
        mileage.point = mileageDto.getPoint();
        mileage.setMember(member);
        return mileage;
    }

}
