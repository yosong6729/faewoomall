package com.example.faewoomall.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDTO {

    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 1, max = 30)
    private String userId;
    @Column(unique = true)
    @NotBlank
    private String email;
    @NotBlank
    private String authenticationNum;
    private String mailCheck; //메일확인 TRUE, FALSE
    @NotBlank
    private String password;
    @NotBlank
    private String checkPassword;

    private String zipcode; //나중에 주소 하나로 묶어서 해야함

    private String streetAdr;

    private String detailAdr;
}
