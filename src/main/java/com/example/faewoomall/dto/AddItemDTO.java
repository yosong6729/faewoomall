package com.example.faewoomall.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class AddItemDTO {
    private String name; //상품명
    private int price;
    private int stockQuantity;
    private String category;
    private String saleStatus;
    private String content;
    private List<MultipartFile> imageFiles;
}
