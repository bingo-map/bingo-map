package com.bingomap.bingo_map.dto;

import lombok.Getter;
import lombok.Setter;

// Entity(RESTAURANT 테이블 복사본)에 있는 값 중에서
// 화면에 실제로 보여줄 것들만 그대로 옮겨 담는 상자예요.
@Getter
@Setter
public class RestaurantDto {
    // Entity(2번에서 만든 Restaurant.java)랑 변수 이름을 똑같이 맞췄어요.
    // 이렇게 해두면 5번(Service)에서 "Entity 값을 DTO로 옮겨 담기"가 훨씬 쉬워져요.
    private Long id;
    private String name;
    private String category;
    private String tags;
    private Double rating;
    private Integer reviewCount;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private String openingHours;
    private String phone;
    private String priceRange;
    private String websiteUrl;
    private String seatInfo;
    private String reservationInfo;
    private String paymentMethods;
    private String languages;
    private String mainImageUrl;
}