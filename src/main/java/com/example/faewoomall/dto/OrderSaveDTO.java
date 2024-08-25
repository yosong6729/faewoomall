package com.example.faewoomall.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderSaveDTO {

    private List<OrderSaveDetailDTO> orderSaveDetailDTOList;
//    private int quantity;
//    private int price;
//    private String phoneNumber;
    private String zipcode; //주소 한가지로 모으기 Embaded
    private String streetAdr;
    private String detailAdr;

    @Override
    public String toString() {
        return "OrderSaveDTO{" +
                "orderSaveDetailDTOList=" + orderSaveDetailDTOList +
                ", zipcode='" + zipcode + '\'' +
                ", streetAdr='" + streetAdr + '\'' +
                ", detailAdr='" + detailAdr + '\'' +
                '}';
    }
}
