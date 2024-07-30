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

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private  List<EventLink> content;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<eventImg> eventImgs; // itemImg 엔티티와의 일대다 관계




}
