package com.trip.service;

import com.trip.dto.*;
import com.trip.entity.*;
import com.trip.repository.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private  final ItemRepository itemRepository;
    private  final MemberRepository memberRepository;
    private  final CartRepository cartRepository;
    private  final CartItemRepository cartItemRepository;
    private  final  OrderService orderService;
    private  final ViewRepository viewRepository;

    @Transactional(readOnly = true)
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
        System.out.println("asallasdjlksadjls");
        //이메일로 멤버 객체 추출
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityExistsException::new);
        System.out.println("1111111111111111111");
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

    public  Long addCart(CartItemDto cartItemDto, String email){
        Member member = memberRepository.findByEmail(email); //쿼리문 날려서 멤버 객체 빼옴

        Cart cart = cartRepository.findByMemberId(member.getId());
        if (cart==null){ //카드 ==null, 회원가입 한 적이 없다. ==> 첫 가입 고객
            cart = Cart.creatCart(member); //cart 생성
            cartRepository.save(cart);  //cart DB에 저장
        }
        //else인 경우, 회원가입 된 회원. 그냥 내려옴
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityExistsException::new);//쿼리문 날려서 cartitem 정보 객체 빼옴


        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if (savedCartItem != null){
            // cartitem에 객체가 있다. == 장바구니에 이미 담겨 있다. ==> 수량이 올라간다.(변경감지) _ save 안함
            savedCartItem.addCount(cartItemDto.getCount());
            return  savedCartItem.getId();
        }
        else {
            //객체가 없다. == 장바구니에 처음 담긴다. ==> DB에 저장을 해야한다.
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }


    }

    public Long orderCartItem(CartOrderDto cartOrderDto, String email){
        //주문 dto list 객체 생성
        List<OrderDto> orderDtoList = new ArrayList<>();
        //주문 리스트에 있는 목록을 카트 아이템 객체로 추출.
        //주문 dto에 카트 아이템 정보를 담고, 위에 선언된 주문 dto list에 추가한다.

            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId())
                    .orElseThrow(EntityExistsException::new);
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());

        // 주문 dtolist랑 현재 로그인 된 email을 매개 변수로 넣어서 주문
        Long orderId = orderService.order(orderDto, email);

        //카트에 있는 아이템이 주문 되니까 해당 아이템을 모두 삭제한다.
            cartItemRepository.delete(cartItem);

        return orderId;
    }
    public Page<MainItemDto> MemberItemPage(Pageable pageable, List<Viewer> viewerList){
        return itemRepository.MemberItemPage(pageable, viewerList);
    }


}
