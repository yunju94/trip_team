package com.trip.service;

import com.trip.dto.EventFormDto;
import com.trip.dto.EventImgDto;
import com.trip.dto.ItemFormDto;
import com.trip.dto.ItemImgDto;
import com.trip.entity.Item;
import com.trip.entity.ItemImg;
import com.trip.entity.event;
import com.trip.entity.eventImg;
import com.trip.repository.EventImgReposotory;
import com.trip.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private  final  EventImgService eventImgService;
    private  final EventImgReposotory eventImgReposotory;
    private  final  EventLinkService eventLinkService;

    public Long saveEventTem(EventFormDto eventFormDto, List<MultipartFile> eventImgFileList)
            throws Exception{
        //상품등록
        event even = eventFormDto.createEventTem();
        eventRepository.save(even);

        //이미지 등록
        eventLinkService.saveEventLinkcontet(eventFormDto, even);
        for (int i=0; i<eventImgFileList.size();i++) {
            eventImg eventImg= new eventImg();
            eventImg.setEvent(even);
            if (i==0)
                eventImg.setReqImgYn("Y");
            else
                eventImg.setReqImgYn("N");
            eventImgService.saveEventImg(eventImg,eventImgFileList.get(i));
        }

        return even.getId();
    }

    public List<event> AllSearch(){
        return eventRepository.findAll();
    }

    public   Optional<event> seachEvent(Long eventId){
        return eventRepository.findById(eventId);
    }




    @Transactional(readOnly = true)
    public EventFormDto geteventIdDtl(Long eventId) {
        //Entity
        List<eventImg> eventImgList = eventImgReposotory.findByeventIdOrderByIdAsc(eventId);
        //DB에서 데이터를 가지고 온다.
        //DTO
        List<EventImgDto> eventImgDtoList = new ArrayList<>();

        for (eventImg eventImg : eventImgList) {
            // Entity -> DTO
            EventImgDto eventImgDto = EventImgDto.of(eventImg);
            eventImgDtoList.add(eventImgDto);
        }

        event even = eventRepository.findById(eventId).orElseThrow(EntityNotFoundException::new);
        // Item -> ItemFormDto modelMapper
        EventFormDto eventFormDto = EventFormDto.of(even);
        eventFormDto.setEventImgDtoList(eventImgDtoList);
        return eventFormDto;
    }



    public Long updateItem(EventFormDto eventFormDto, List<MultipartFile> itemImgFileList)
            throws Exception{
        //상품 변경
        event event =eventRepository.findById(eventFormDto.getId()).
                orElseThrow(EntityNotFoundException::new);
        //상품 이미지 변경
        List<Long> eventImgIds = eventFormDto.getEventImgIds();

        if (eventFormDto.getEventImgIds().size()!=0){
            for (int i=0; i<itemImgFileList.size();i++){
                eventImgService.updateItemImg(eventImgIds.get(i),itemImgFileList.get(i));
            }
        }

        if (eventFormDto.getContent().size()!=0){

            //상품 링크 변경
            for (int i = 0 ; i< eventFormDto.getContent().size(); i++){
                eventLinkService.updateLink(eventFormDto, event.getId());
            }

        }



        return event.getId();
    }

    public void  getEventDelete(Long Id){
       event event= eventRepository.findById(Id).
               orElseThrow(EntityNotFoundException::new);

      List<eventImg> eventImg= eventImgReposotory.findByeventIdOrderByIdAsc(event.getId());
      for (int i = 0 ; i<eventImg.size() ; i++){
          eventImgReposotory.delete(eventImg.get(i));
      }

       eventRepository.delete(event);


    }


}
