package com.example.faewoomall.service;

import com.example.faewoomall.domain.Cart;
import com.example.faewoomall.domain.Item;
import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.OrderListDTO;
import com.example.faewoomall.dto.OrderSaveDetailDTO;
import com.example.faewoomall.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;

    public void cartListDelete(OrderListDTO orderListDTOList) {

        List<OrderSaveDetailDTO> orderSaveDetailDTOList = orderListDTOList.getOrderSaveDetailDTOList();
        for (OrderSaveDetailDTO orderSaveDetailDTO : orderSaveDetailDTOList) {
            Long cartId = orderSaveDetailDTO.getCartId();
            cartRepository.deleteById(cartId);
            log.info("삭제된 cartId = {}", cartId);
        }
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public List<Cart> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public void saveCart(User user, Item item, int quantity, String size) {
        Cart cart = new Cart();
        cart.setItem(item);
        cart.setUser(user);
        cart.setQuantity(quantity);
        cart.setSize(size);

        cartRepository.save(cart);
    }

    public int totalOrderAmount(Long userId) {

        int totalOrderAmount = 0;
        List<Cart> carts = cartRepository.findByUserId(userId);

        for (Cart cart : carts) {
            totalOrderAmount += (cart.getQuantity() * cart.getItem().getPrice());
        }

        return totalOrderAmount;
    }
}
