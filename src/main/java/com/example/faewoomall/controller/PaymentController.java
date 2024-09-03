package com.example.faewoomall.controller;

import com.example.faewoomall.domain.Cart;
import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.*;
import com.example.faewoomall.service.CartService;
import com.example.faewoomall.service.OrderService;
import com.example.faewoomall.service.UserService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final UserService userService;
    private final OrderService orderService;
    private final CartService cartService;

    private IamportClient iamportClinet;

    @Value("${IMP_API_KEY}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init() {
        this.iamportClinet = new IamportClient(apiKey, secretKey);
    }

    @PostMapping("/payment")
    public String payment(/*@RequestParam Long userId,
                          @RequestParam int totalOrderAmount,*/
            @ModelAttribute PaymentRequestDTO paymentRequestDTO, Model model) {
        log.info("userId = {}", paymentRequestDTO.getUserId());
        log.info("totalOrderAmount = {}", paymentRequestDTO.getTotalOrderAmount());
        User user = userService.findById(paymentRequestDTO.getUserId());
        List<Cart> carts = user.getCarts();
        List<Long> cartsId = new ArrayList<>();

        for (Cart cart : carts) {
            cartsId.add(cart.getId());
            log.info("cartId = {}", cart.getId());
        }

        model.addAttribute("name", user.getName());
        model.addAttribute("deliveryInfoDTO", new DeliveryInfoDTO());
        model.addAttribute("user", user);
        model.addAttribute("totalOrderAmount", paymentRequestDTO.getTotalOrderAmount());
        model.addAttribute("cartsId", cartsId);

        return "payment";
    }

    @PostMapping("/payment/validation/{imp_uid}")
    @ResponseBody
    public IamportResponse<Payment> validateIamport(@PathVariable String imp_uid) throws IamportResponseException, IOException {
        log.info("validateIamport");
        IamportResponse<Payment> payment = iamportClinet.paymentByImpUid(imp_uid);
        log.info("결제 요청 응답. 결제 내역 - 주문 번호 = {}", payment.getResponse().getMerchantUid());

        return payment;
    }

    /**
     * 장바구니 결제
     */
    @PostMapping("/order/payment")
    public ResponseEntity<Map<String, List<OrderSaveDetailDTO>>> paymentComplete(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @RequestBody OrderSaveDTO orderSaveDTO) {


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

        try {
            log.info("size = {}", orderSaveDTO.getOrderSaveDetailDTOList().size());
        } catch (NullPointerException e) {
            log.info("size == 0");
        }

        log.info("orderSaveDTOs = {}", orderSaveDTO);

        //User객체와 Carts리스트 넘기기
        List<Cart> cartList = cartService.findByUserId(userId);
        orderService.orderSave(orderSaveDTO, user, cartList);
        Map<String, List<OrderSaveDetailDTO>> response = new HashMap<>();
        List<OrderSaveDetailDTO> orderSaveDetailDTOList = orderSaveDTO.getOrderSaveDetailDTOList();
        response.put("orderSaveDetailDTOList", orderSaveDetailDTOList);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cartList/delete")
    public ResponseEntity<?> deleteCartList(@RequestBody OrderListDTO orderListDTOList) {
        log.info("orderListDTOList = {}", orderListDTOList);

        cartService.cartListDelete(orderListDTOList);

        Map<String, String> response = new HashMap<>();
        response.put("message", "장바구니 삭제 성공");

        return ResponseEntity.ok(response);
    }

    /**
     * 배송지 변경
     */
    @GetMapping("/delivery/edit")
    public String editDeliveryAdr() {

        return "editDeliveryAdr";
    }




    @Getter
    @Setter
    public static class PaymentRequestDTO {
        private Long userId;
        private int totalOrderAmount;
    }


    @Getter
    @Setter
    public static class TotalOrderAmountDTO {
        private int totalOrderAmount;
    }


}
