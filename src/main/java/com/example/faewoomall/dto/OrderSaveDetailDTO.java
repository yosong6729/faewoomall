package com.example.faewoomall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSaveDetailDTO {

    private Long userId;
    private Long cartId;

    @Override
    public String toString() {
        return "OrderSaveDetailDTO{" +
                "userId=" + userId +
                ", cartId=" + cartId +
                '}';
    }
}
