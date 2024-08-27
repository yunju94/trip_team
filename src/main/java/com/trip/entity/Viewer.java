package com.trip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "viewer")
@Getter
@Setter
public class Viewer {

    @Id
    @Column(name = "viewer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    public static Viewer saveViewer(Member member, Item item){
        Viewer viewer = new Viewer();
        viewer.setMember(member);
        viewer.setItem(item);
        viewer.setCount(1);

        return viewer;

    }



}
