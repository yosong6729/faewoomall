package com.example.faewoomall.domain;


public enum SaleStatus {

    SELLING("판매 중"), SOLDOUT("판매 완료"), WAITING("판매 대기");

    private final String description;

    SaleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
