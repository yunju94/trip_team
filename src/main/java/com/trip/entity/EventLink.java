package com.trip.entity;

import com.trip.dto.EventFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "eventlink")
@Getter
@Setter
public class EventLink {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "eventlink_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private event event;

    @Column(columnDefinition = "TEXT")
    private  String link;

    public void updateEventLink(EventFormDto eventFormDto){
        for (int i = 0 ; i < eventFormDto.getContent().size(); i++){
            this.link=eventFormDto.getContent().get(i).getLink();
        }

    }


}
