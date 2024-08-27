package com.trip.service;

import com.trip.entity.Item;
import com.trip.entity.Member;
import com.trip.entity.Viewer;
import com.trip.repository.ItemRepository;
import com.trip.repository.MemberRepository;
import com.trip.repository.ViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ViewService {
    private final MemberRepository memberRepository;
    private final ViewRepository viewRepository;
    private  final ItemRepository itemRepository;
    public void  savememberItem(Member member , Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow();
        Viewer viewer = viewRepository.findByMemberIdAndItemId(member.getId(), itemId);
        if (viewer== null){
            Viewer viewers = Viewer.saveViewer(member, item);
            viewRepository.save(viewers);
        }else {
            viewer.setCount(viewer.getCount()+1);
        }


    }

    public List<Viewer> memberSearch(String email){
        Member member = memberRepository.findByEmail(email);
       return viewRepository.findByMemberIdOrderByCountAsc(member.getId());
    }

    public  void  deleteItem(Long itemId){

        Viewer viewer = viewRepository.findByItemId(itemId);
        viewRepository.delete(viewer);

    }
}
