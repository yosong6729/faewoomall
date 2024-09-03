package com.example.faewoomall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectOrderDTO {

    private Long userId;
    private Long itemId;
    private String size;
    private int quantity;
    private String zipcode;
    private String streetAdr;
    private String detailAdr;

}
