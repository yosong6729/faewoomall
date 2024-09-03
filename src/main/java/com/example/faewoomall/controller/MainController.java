package com.example.faewoomall.controller;

import com.example.faewoomall.domain.Item;
import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.CustomOAuth2User;
import com.example.faewoomall.dto.CustomUserDetails;
import com.example.faewoomall.service.ItemService;
import com.example.faewoomall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final ItemService itemService;
    private final UserService userService;

    /**
     * 처음 메인 페이지
     */
    @GetMapping("/")
    public String homeP(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            /*@AuthenticationPrincipal CustomOAuth2User customOAuth2User,*/
            /*Authentication authentication,*/
                        Model model) {
        Page<Item> allItem = null;
        if (keyword != null) {
            allItem = itemService.pagedItemList(page, keyword);
        } else {
            allItem = itemService.findAll(page);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userEmail = null;
        String name = null;
        User user = null;
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userEmail = userDetails.getUsername();
            user = userService.findByEmail(userEmail);
            name = user.getName();
            Long userId = user.getId();
            log.info("name, userId = {}, {}", name, userId);
            model.addAttribute("userId", userId);
        } catch (ClassCastException e) {
            try {
                CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
                String oAuth2Id = userDetails.getOAuth2Id();
                user = userService.findByOAuth2Id(oAuth2Id);
                name = user.getName();
                Long userId = user.getId();
                log.info("userEmail, userId = {}, {}", name, userId);
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
        log.info("itemInWishListMap = {}", itemInWishListMap);

        log.info("name = {}, role = {}", name, role);

        model.addAttribute("name", name);
        model.addAttribute("items", allItem);
        model.addAttribute("user", user);
        model.addAttribute("itemInWishListMap", itemInWishListMap);

        return "home";
    }
}
