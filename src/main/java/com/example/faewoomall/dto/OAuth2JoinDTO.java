package com.example.faewoomall.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2JoinDTO {

    private String name;
    private String zipcode; //나중에 주소 하나로 묶어서 해야함
    private String streetAdr;
    private String detailAdr;
}
