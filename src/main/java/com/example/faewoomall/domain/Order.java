package com.example.faewoomall.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String itemName; //상품명
    private String storedFileName; //상품사진
    private int price; //가격
    private int quantity; //수량
    private String size; //사이즈
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    private String zipcode; //우편주소 ,주소 한가지로 모으기 Embaded
    private String streetAdr; //도로명 주소
    private String detailAdr; //상세 주소

    @CreationTimestamp
    private LocalDate createDate;
}
