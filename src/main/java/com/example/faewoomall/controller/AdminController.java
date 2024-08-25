package com.example.faewoomall.controller;

import com.example.faewoomall.domain.*;
import com.example.faewoomall.dto.AddItemDTO;
import com.example.faewoomall.dto.EditUserDTO;
import com.example.faewoomall.service.FileStoreService;
import com.example.faewoomall.service.ItemService;
import com.example.faewoomall.service.OrderService;
import com.example.faewoomall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final ItemService itemService;
    private final FileStoreService fileStoreService;
    private final OrderService orderService;

    /**
     * 회원관리 페이지(페이징 처리)
     */
    @GetMapping("/users")
    public String userList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            Model model) {

        log.info("keyword = {}", keyword);

        Page<User> pagingUsers = null;
        if (keyword == null) {
            pagingUsers = userService.getPagingUsers(page);
        } else if (keyword != null) {
            pagingUsers = userService.searchingUserList(keyword, page);
        }

        model.addAttribute("users", pagingUsers);

        return "users";
    }

    /**
     * 상품 목록 조회
     */
    @GetMapping("/items")
    public String itemList(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Item> all = itemService.findAll(page);
        model.addAttribute("items", all);

        return "items";
    }


    /**
     * 사진 가져오기
     */
    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource showImages(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStoreService.getFullPath(fileName));
    }



}
