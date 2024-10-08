package com.example.faewoomall.controller;

import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.CartIdDTO;
import com.example.faewoomall.dto.CustomOAuth2User;
import com.example.faewoomall.dto.CustomUserDetails;
import com.example.faewoomall.service.CartService;
import com.example.faewoomall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final UserService userService;
    private final CartService cartService;

    /**
     * 사용자 장바구니 페이지
     */
    @GetMapping("/cart")
    public String cartP(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                        @AuthenticationPrincipal CustomOAuth2User customOAuth2User, Model model) {

        Long userId = null;
        User user = null;
        try {
            String email = customUserDetails.getUsername();
            user = userService.findByEmail(email);
            userId = user.getId();
        } catch (NullPointerException e) {
            log.info("customUserDetails.getUsername() is null");
        }

        try {
            String username = customOAuth2User.getOAuth2Id();
            user = userService.findByOAuth2Id(username);
            userId = user.getId();
        } catch (NullPointerException e) {
            log.info("customOAuth2User.getUsername() is null");
        }

        int totalOrderAmount = cartService.totalOrderAmount(userId);

        model.addAttribute("name", user.getName());
        model.addAttribute("user", user);
        model.addAttribute("totalOrderAmount", totalOrderAmount);

        return "cart";
    }

    @PostMapping ("/cart/delete")
    @ResponseBody
    public ResponseEntity<String> deleteCartItem(@RequestBody CartIdDTO cartIdDTO) {
        log.info("cartId = {}", cartIdDTO.getCartId());
        cartService.deleteCartById(cartIdDTO.getCartId());
        return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
    }
}
