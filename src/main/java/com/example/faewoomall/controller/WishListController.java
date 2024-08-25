package com.example.faewoomall.controller;

import com.example.faewoomall.domain.Item;
import com.example.faewoomall.domain.User;
import com.example.faewoomall.domain.WishList;
import com.example.faewoomall.dto.CustomOAuth2User;
import com.example.faewoomall.dto.CustomUserDetails;
import com.example.faewoomall.dto.ItemIdDTO;
import com.example.faewoomall.service.ItemService;
import com.example.faewoomall.service.UserService;
import com.example.faewoomall.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WishListController {

    private final UserService userService;
    private final ItemService itemService;
    private final WishListService wishListService;

    /**
     * 찜 목록 조회
     */
    @GetMapping("/wishList")
    public String wishListP(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        User user = null;
        try {
            String email = customUserDetails.getUsername();
            user = userService.findByEmail(email);
        } catch (NullPointerException e) {
            log.info("customUserDetails.getUsername() is null");
        }

        try {
            String username = customOAuth2User.getOAuth2Id();
            user = userService.findByEmail(username);
        } catch (NullPointerException e) {
            log.info("customOAuth2User.getUsername() is null");
        }
        Page<WishList> pagedWishList = wishListService.findPagedWishList(page);
        log.info("pagedWishList.getTotalPages() = {}", pagedWishList.getTotalPages());
        log.info("pagedWishList.getTotalElements() = {}", pagedWishList.getTotalElements());

        model.addAttribute("name", user.getName());
        model.addAttribute("pagedWishList", pagedWishList);

        return "wishList";
    }


    /**
     * 찜 하기
     */
    @PostMapping("/wishList/add")
    public ResponseEntity<?> addWhishList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody ItemIdDTO itemIdDTO
    ) {

        log.info("itemIdDTO = {}", itemIdDTO.getItemId());
        Item item = itemService.findById(itemIdDTO.getItemId());

        User user = null;
        try {
            String email = customUserDetails.getUsername();
            user = userService.findByEmail(email);
        } catch (NullPointerException e) {
            log.info("customUserDetails.getUsername() is null");
        }

        try {
            String username = customOAuth2User.getOAuth2Id();
            user = userService.findByEmail(username);
        } catch (NullPointerException e) {
            log.info("customOAuth2User.getUsername() is null");
        }

        wishListService.saveWishList(item, user);

        return ResponseEntity.ok("찜 리스트 저장");
    }

    /**
     * 찜 취소
     */
    @PostMapping("/wishList/delete")
    public ResponseEntity<?> deleteWhishList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody ItemIdDTO itemIdDTO) {

        log.info("itemIdDTO = {}", itemIdDTO.getItemId());
        Item item = itemService.findById(itemIdDTO.getItemId());

        User user = null;
        try {
            String email = customUserDetails.getUsername();
            user = userService.findByEmail(email);
        } catch (NullPointerException e) {
            log.info("customUserDetails.getUsername() is null");
        }

        try {
            String username = customOAuth2User.getOAuth2Id();
            user = userService.findByEmail(username);
        } catch (NullPointerException e) {
            log.info("customOAuth2User.getUsername() is null");
        }

        Long wishListId = null;
        List<WishList> wishLists = user.getWishLists();
        for (WishList wishList : wishLists) {
            if (Objects.equals(wishList.getItem().getId(), itemIdDTO.getItemId())) {
                wishListId = wishList.getId();
            }
        }

        wishListService.deleteWishList(wishListId);

        return ResponseEntity.ok("찜 삭제 성공");
    }
}
