package com.example.faewoomall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryInfoDTO {

    private String userName;
    private String zipcode; //주소 한가지로 모으기 Embaded
    private String streetAdr;
    private String detailAdr;
    private String phoneNum;
}
