package com.example.faewoomall.service;

import com.example.faewoomall.domain.Cart;
import com.example.faewoomall.domain.Order;
import com.example.faewoomall.domain.OrderStatus;
import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.OrderSaveDTO;
import com.example.faewoomall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public Page<Order> searchingOrderList(int page, String keyword) {
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(Sort.Order.desc("createDate"));

        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by(sort));
        return orderRepository.findByItemNameContaining(keyword, pageRequest);
    }

    public Page<Order> findAll(int page) {

        List<Sort.Order> sort = new ArrayList<>();
        sort.add(Sort.Order.desc("createDate"));

        PageRequest pageRequest = PageRequest.of(page, 6, Sort.by(sort));
        return orderRepository.findAll(pageRequest);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    //장바구니 구매시 주문정보 저장
    @Transactional
    public void orderSave(OrderSaveDTO orderSaveDTO, User user, List<Cart> cartList) {

        for (Cart cart : cartList) {
            Order order = new Order();
            order.setUser(user);
            order.setItemName(cart.getItem().getName());
            order.setPrice(cart.getItem().getPrice());
            order.setOrderStatus(OrderStatus.PREPARE);
            order.setStoredFileName(cart.getItem().getImageFiles().get(0).getStoredFileName());
            order.setQuantity(cart.getQuantity());
            order.setZipcode(orderSaveDTO.getZipcode());
            order.setStreetAdr(orderSaveDTO.getStreetAdr());
            order.setDetailAdr(orderSaveDTO.getDetailAdr());
            Order saveOrder = orderRepository.save(order);
            log.info("saveOrderId = {}", saveOrder);
        }
    }

    @Transactional
    public void editOrderStatus(List<String> orderIdList, String orderStatus) {
        log.info("editOrderStatus실행");

        for (String orderId : orderIdList) {
            Order order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow();
            log.info("orderId = {}", orderId);
            order.setOrderStatus(OrderStatus.valueOf(orderStatus));
            log.info("orderStatus = {}", orderStatus);
        }
        log.info("editOrderStatus종료");
    }

    public void deleteOrder(List<String> orderIdList) {

        for (String orderId : orderIdList) {
            orderRepository.deleteById(Long.valueOf(orderId));
        }
    }
}
