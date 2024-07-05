package com.trip.controller;

import com.trip.dto.CartDetailDto;
import com.trip.dto.ItemSearchDto;
import com.trip.dto.MainItemDto;
import com.trip.entity.CartItem;
import com.trip.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class CartController {

    private  final CartService cartService;

    @GetMapping(value = "/cart")
    public String main(Principal principal, Model model) {
        if (principal ==null){ //만약에 개인정보가 없으면 로그인으로 이동시킨다.
            model.addAttribute("errorMessage", "로그인 후 이용하시기 바랍니다.");
            return "member/memberLoginForm";
        }
        //개인정보가 있으면 그 뒤에 진행.
        //아이템과 오더, 카트를 조인한 후 거기에 필요한 정보를 빼와서 리스트로 만든다.
        List<CartDetailDto> cartDetailItem = cartService.getCartList(principal.getName());
        model.addAttribute("cartitem", cartDetailItem);
        return "cart/cartList";
    }

    @PostMapping(value = "/cart/{cartItemId}/delete")
    public  @ResponseBody ResponseEntity cartItemDelete(@PathVariable("cartItemId")  Long cartItemId
                                            , Principal principal){
        if (!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

}
