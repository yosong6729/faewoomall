package com.example.faewoomall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUserDTO {

    private String userId;
    private String nickName;
    private String password;
    private String zipcode;
    private String streetAdr;
    private String detailAdr;

}
