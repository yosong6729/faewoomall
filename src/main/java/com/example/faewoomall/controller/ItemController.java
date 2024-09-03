package com.example.faewoomall.controller;

import com.example.faewoomall.domain.Item;
import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.CustomOAuth2User;
import com.example.faewoomall.dto.CustomUserDetails;
import com.example.faewoomall.dto.SaveToCartDTO;
import com.example.faewoomall.service.CartService;
import com.example.faewoomall.service.ItemService;
import com.example.faewoomall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final UserService userService;
    private final CartService cartService;

    /**
     * 상의 목록 페이지
     */
    @GetMapping("/TOP")
    public String topP(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Item> allItem = itemService.findAllByCategoryIsTop(page);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = null;
        User user = null;
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();
            user = userService.findByEmail(email);
            Long userId = user.getId();
            log.info("email, userId = {}, {}", email, userId);
            model.addAttribute("userId", userId);
        } catch (ClassCastException e) {
            try {
                CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
                String oAuth2Id = userDetails.getOAuth2Id();
                user = userService.findByOAuth2Id(oAuth2Id);
                Long userId = user.getId();
                email = user.getEmail();
                log.info("email, userId = {}, {}", email, userId);
                model.addAttribute("userId", userId);
            } catch (ClassCastException exception) {
                log.info("CustomAuth2User, CustomUSerDetails 둘다 ClassCastException 발생");
            }
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        HashMap<Long, Boolean> itemInWishListMap = new HashMap<>();
        if (user != null) {
            log.info("user.getWishList = {}", user.getWishLists().size());
            for (Item item : allItem) {
                boolean isInWishList = user.getWishLists().stream().anyMatch(wishList ->
                        wishList.getItem().getId().equals(item.getId()));
                log.info("itemId = {}, isInWishList = {}", item.getId(), isInWishList);
                itemInWishListMap.put(item.getId(), isInWishList);
            }
        }

        log.info("email = {}, role = {}", email, role);

        if (user != null) {
            model.addAttribute("name", user.getName());
        }
        model.addAttribute("items", allItem);
        model.addAttribute("user", user);
        model.addAttribute("itemInWishListMap", itemInWishListMap);

        return "home";
    }


    /**
     * 하의 목록 페이지
     */
    @GetMapping("/PANTS")
    public String pantsP(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Item> allItem = itemService.findAllByCategoryIsPants(page);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = null;
        User user = null;
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            email = userDetails.getUsername();
            user = userService.findByEmail(email);
            Long userId = user.getId();
            log.info("email, userId = {}, {}", email, userId);
            model.addAttribute("userId", userId);
        } catch (ClassCastException e) {
            try {
                CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
                String oAuth2Id = userDetails.getOAuth2Id();
                user = userService.findByOAuth2Id(oAuth2Id);
                Long userId = user.getId();
                email = user.getEmail();
                log.info("email, userId = {}, {}", email, userId);
                model.addAttribute("userId", userId);
            } catch (ClassCastException exception) {
                log.info("CustomAuth2User, CustomUSerDetails 둘다 ClassCastException 발생");
            }
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        HashMap<Long, Boolean> itemInWishListMap = new HashMap<>();
        if (user != null) {
            log.info("user.getWishList = {}", user.getWishLists().size());
            for (Item item : allItem) {
                boolean isInWishList = user.getWishLists().stream().anyMatch(wishList ->
                        wishList.getItem().getId().equals(item.getId()));
                log.info("itemId = {}, isInWishList = {}", item.getId(), isInWishList);
                itemInWishListMap.put(item.getId(), isInWishList);
            }
        }

        log.info("email = {}, role = {}", email, role);
        if (user != null) {
            model.addAttribute("name", user.getName());

        }
        model.addAttribute("items", allItem);
        model.addAttribute("itemInWishListMap", itemInWishListMap);
        model.addAttribute("user", user);

        return "home";
    }

    /**
     * 상품 상세 페이지
     */
    @GetMapping("/item/{itemId}")
    public String item(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                       @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                       @PathVariable Long itemId, Model model) {

        Long userId = null;
        User user = null;
        try {
            String username = customUserDetails.getUsername();
            user = userService.findByEmail(username);
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

        log.info("item");

        Item item = itemService.findById(itemId); //Optional 처리해야힘
        if (user != null) {
            model.addAttribute("name", user.getName());
        }
        model.addAttribute("item", item);
        model.addAttribute("userId", userId);

        return "item";
    }

    /**
     * 상품 상세설명 페이지
     */
    @PostMapping("/item/{itemId}")
    public ResponseEntity<String> item(/*@AuthenticationPrincipal CustomUserDetails customUserDetails,*/
            @RequestBody SaveToCartDTO saveToCartDTO,
            @PathVariable Long itemId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        log.info("상품 저장 Controller");
        Item item = itemService.findById(itemId);
        log.info("quantity = {}", saveToCartDTO.getQuantity());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            username = oAuth2User.getOAuth2Id();
            log.info("oAuth2User.getUsername(), username = {}, {}", oAuth2User.getOAuth2Id(), username);
        } catch (ClassCastException e) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
            log.info("userDetails.getUsername() = {}", userDetails.getUsername());
        }

        log.info("size = {}", saveToCartDTO.getSize());
        User user = userService.findByEmail(username);
        cartService.saveCart(user, item, saveToCartDTO.getQuantity(), saveToCartDTO.getSize());

        return ResponseEntity.ok("저장 완료");
    }

}
