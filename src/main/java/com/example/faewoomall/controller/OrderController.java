package com.example.faewoomall.controller;

import com.example.faewoomall.domain.Order;
import com.example.faewoomall.domain.OrderStatus;
import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.CustomOAuth2User;
import com.example.faewoomall.dto.CustomUserDetails;
import com.example.faewoomall.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final UserService userService;

    /**
     * 사용자 주문목록 이동
     */
    @GetMapping("/order/list")
    public String myOrderP(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                           @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                           Model model) throws JsonProcessingException {

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
            user = userService.findByEmail(username);
            userId = user.getId();
        } catch (NullPointerException e) {
            log.info("customOAuth2User.getUsername() is null");
        }

        model.addAttribute("orders", user.getOrders());

        //같은 order의 같은날짜끼리 묶어야함
        //컨틀러단에서 order의 createDate의 날짜가 같으면 하나로 묶기?
        //그거에 맞기 DTO생성해서 보내줘야하나?
        List<Order> orders = user.getOrders();
        UserOrderListDTO userOrderListDTO = new UserOrderListDTO();
        UserOrderDetailDTO userOrderDetailDTO = new UserOrderDetailDTO();
        String createDate = null;
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (Order order : orders) {
            //만약 order의 createDate값이 같으면
            //원래 있던 List<OrderDTO>에 add
            //다르면
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setItemName(order.getItemName());
            orderDTO.setQuantity(order.getQuantity());
            orderDTO.setPrice(order.getPrice());
            orderDTO.setOrderStatus(order.getOrderStatus());
            orderDTO.setSize(order.getSize());
            orderDTO.setStoredFileName(order.getStoredFileName());
            log.info("주문 날짜 = {}", order.getCreateDate());
            if (createDate == null) {
                userOrderDetailDTO = new UserOrderDetailDTO();
                createDate = String.valueOf(order.getCreateDate());
                log.info("createDate = {}", createDate);
                userOrderDetailDTO.setDate(String.valueOf(order.getCreateDate()));
                userOrderDetailDTO.getOrderDTOList().add(orderDTO);
                log.info("날짜 null인 경우 = {}", orderDTO);
            } else if (createDate.equals(String.valueOf(order.getCreateDate()))) {
                userOrderDetailDTO.getOrderDTOList().add(orderDTO);
                log.info("날짜 변경되지 않은 경우 = {}", orderDTO);
            } else {
                userOrderListDTO.getUserOrderDetailDTOList().add(userOrderDetailDTO);
                userOrderDetailDTO = new UserOrderDetailDTO();
                createDate = String.valueOf(order.getCreateDate());
                log.info("createDate = {}", createDate);
                userOrderDetailDTO.setDate(String.valueOf(order.getCreateDate()));
                userOrderDetailDTO.getOrderDTOList().add(orderDTO);
                log.info("날짜 변경된 경우 = {}", orderDTO);
            }
        }
        userOrderListDTO.getUserOrderDetailDTOList().add(userOrderDetailDTO);

        model.addAttribute(userOrderListDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("userOrderListDTO = {}", objectMapper.writeValueAsString(userOrderListDTO));
        log.info("getUserOrderDetailDTOList의 size = {}", userOrderListDTO.getUserOrderDetailDTOList().size());
        model.addAttribute("name", user.getName());

        return "userOrders";
    }

    @Getter
    @Setter
    public static class UserOrderListDTO{

        private List<UserOrderDetailDTO> userOrderDetailDTOList = new ArrayList<>();

    }

    @Getter
    @Setter
    public static class UserOrderDetailDTO {

        private String date;
        private List<OrderDTO> orderDTOList = new ArrayList<>();

    }

    @Getter
    @Setter
    public static class OrderDTO {
        private String itemName; //상품명
        private int price; //가격
        private int quantity; //주문개수
        private String storedFileName;
        private OrderStatus orderStatus;
        private String size;
    }
}
