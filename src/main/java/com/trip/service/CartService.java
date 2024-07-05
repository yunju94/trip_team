package com.trip.service;

import com.trip.dto.CartDetailDto;
import com.trip.entity.Cart;
import com.trip.entity.CartItem;
import com.trip.entity.Member;
import com.trip.repository.CartItemRepository;
import com.trip.repository.CartRepository;
import com.trip.repository.MemberRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {


    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public List<CartDetailDto> getCartList(String email){
        //오더랑 아이템에서 정보를 뽑아낸다.
        //이메일이 있다. 아이템 정보를 가져오기 위해 email을 멤버에 넣자.
        Member member = memberRepository.findByEmail(email);
        //멤버 아이디로 카드에 있는 걸 가져오자.
        Cart cart = cartRepository.findByMemberId(member.getId());
        List<CartDetailDto> cartDatailDtoList = new ArrayList<>();

        if (cart == null){
            return cartDatailDtoList;
        }
        cartDatailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDatailDtoList;

    }

    public boolean validateCartItem(Long cartItemId, String email){

        Member curMember = memberRepository.findByEmail(email);
        //이메일로 멤버 객체 추출
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityExistsException::new);
        //카트 아이템 아이디로 카트 아이템 객체(엔티티) 추출
        Member savedMember = cartItem.getCart().getMember();

        //카트 아이템에 카트 안에 멤버에서 멤버 객체 추출

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            //현재 로그인된 멤버와 카트 아이템에서 뺀 멤버가 같은가
            // 같지 않으면 트루 >> 동작 -> false값 리턴
            return false;
        }

        return true;
    }

    public void deleteCartItem(Long cartItemId){
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
        //찜 목록에서 지우고 DB에서 지운다.
    }
}