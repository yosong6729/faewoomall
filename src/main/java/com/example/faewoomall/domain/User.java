package com.example.faewoomall.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;
    private String name;
    private String email;
    private String password;
    private String zipcode; //주소 한가지로 모으기 Embaded
    private String streetAdr;
    private String detailAdr;
    private String oauth2Id;
    private String role;

    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE) //
    private List<WishList> wishLists = new ArrayList<>();
}
