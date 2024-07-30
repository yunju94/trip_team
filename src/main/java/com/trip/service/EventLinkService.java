package com.trip.service;

import com.trip.dto.EventFormDto;
import com.trip.entity.EventLink;
import com.trip.entity.event;
import com.trip.repository.EventLinkRepository;
import jdk.jfr.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class EventLinkService {
    private final EventLinkRepository eventLinkRepository;
    public void saveEventLinkcontet(EventFormDto eventFormDto, event event){

        System.out.println();
        for (int i =  0 ; i< eventFormDto.getContent().size(); i++){
            EventLink eventLink = new EventLink();
            eventLink.setLink(eventFormDto.getContent().get(i).getLink());
            eventLink.setEvent(event);
            eventLinkRepository.save(eventLink);
            System.out.println("<><><><><><><><><><><><><><><><><><>");
        }

    }
    public  void updateLink(EventFormDto eventFormDto, Long EventId){
        EventLink eventLink = eventLinkRepository.getReferenceById(EventId);
        eventLink.updateEventLink(eventFormDto);
    }
}
