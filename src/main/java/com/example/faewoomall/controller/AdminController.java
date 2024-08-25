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

    /**
     * 관리자 페이지(처음에는 상품 등록 페이지)
     */
    @GetMapping("/item/add")
    public String addItem(Model model) {

        model.addAttribute("item", new Item());

        return "addItem";
    }

    @ModelAttribute("saleStatus")
    public SaleStatus[] saleStatus() {
        return SaleStatus.values();
    }

    @ModelAttribute("categories")
    public Category[] categories() {
        return Category.values();
    }

    @PostMapping("/item/add")
    public String addItem(@ModelAttribute AddItemDTO addItemDTO, Model model) throws IOException {

        log.info("item = {}", addItemDTO);

        itemService.addItem(addItemDTO);

        return "redirect:/item/add";
    }


    /**
     * 회원 삭제
     */
    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam List<String> userIdList) {
        log.info("deleteUser메서드 실행");
        for (String userId : userIdList) {
            log.info("checkedUserId = {}", userId);
        }

        userService.deleteUser(userIdList);

        return "redirect:/users";
    }

    /**
     * 회원 수정 페이지
     */
    @GetMapping("/user/{userId}/edit")
    public String editUserP(@PathVariable Long userId, Model model) {

        User user = userService.findById(userId);
        model.addAttribute("user", user);

        return "editUser";
    }

    /**
     * 회원 수정
     */
    @PutMapping("/user/{userId}/edit")
    public String editUSer(@PathVariable Long userId, @ModelAttribute EditUserDTO editUserDTO) {

        //editUserDTO에 user의 PK인 Id를 추가해서 User를 DB에서 가져오는게 좋은가?
        userService.editUser(userId, editUserDTO);

        return "redirect:/user/{userId}/edit";
    }

    /**
     * 회원 권한 변경
     */
    @PostMapping("/user/role/edit")
    public String editUserRole(
            @RequestParam List<String> userIdList,
            @RequestParam String role) {

        userService.editUserRole(userIdList, role);

        return "redirect:/users";
    }

    /**
     * 주문 목록 페이지
     */
    @GetMapping("/admin/orders")
    public String orders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            Model model) {


        Page<Order> allOrder = null;

        if (keyword == null) {
            allOrder = orderService.findAll(page);

        } else {
            allOrder = orderService.searchingOrderList(page, keyword);
        }

        model.addAttribute("orders", allOrder);

        return "orders";
    }


    /**
     * 주문 상태 수정
     */
    @PostMapping("/admin/order/status/edit")
    public String editOrderStatus(
            @RequestParam List<String> orderIdList,
            @RequestParam String orderStatus) {

        orderService.editOrderStatus(orderIdList, orderStatus);

        log.info("종료");

        return "redirect:/admin/orders";
    }

    /**
     * 주문 목록 삭제
     */
    @PostMapping("/admin/order/delete")
    public String deleteOrder(@RequestParam List<String> orderIdList) {

        orderService.deleteOrder(orderIdList);

        return "redirect:/admin/orders";
    }

}
