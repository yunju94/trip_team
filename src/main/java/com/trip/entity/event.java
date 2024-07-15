package com.trip.entity;

import com.trip.dto.EventFormDto;
import com.trip.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "event")
@Getter
@Setter

public class event extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private Long id;

    private String content;
    private String content1;
    private String content2;
    private String content3;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<eventImg> eventImgs; // itemImg 엔티티와의 일대다 관계


    public void updateEvent(EventFormDto eventFormDto){
        this.content = eventFormDto.getContent();
        this.content1 = eventFormDto.getContent1();
        this.content2 = eventFormDto.getContent2();
        this.content3 = eventFormDto.getContent3();

    }

}
