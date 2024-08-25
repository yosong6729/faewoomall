package com.example.faewoomall.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderListDTO {

    private List<OrderSaveDetailDTO> orderSaveDetailDTOList;

    @Override
    public String toString() {
        return "OrderListDTO{" +
                "orderSaveDetailDTOList=" + orderSaveDetailDTOList +
                '}';
    }
}
