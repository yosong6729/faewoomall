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
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; //상품명
    private int price;
    private int stockQuantity;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;
    private String content;
    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<UploadFile> imageFiles = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Cart> carts;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<WishList> wishLists = new ArrayList<>();

}
