package com.bingomap.bingo_map.dto;

import com.bingomap.bingo_map.Entity.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RestaurantDto {

    private Long restaurantId;
    private String name;
    private String category;
    private String tags;
    private Double rating;
    private Integer reviewCount;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private String openingHours;     // 영업시간
    private String phone;            // 전화번호
    private String priceRange;       // 가격대
    private String websiteUrl;       // 웹사이트
    private String seatInfo;         // 좌석 정보
    private String reservationInfo;  // 예약 정보
    private String paymentMethods;   // 결제 방법
    private String languages;        // 지원 언어
    private String mainImageUrl;     // 대표 이미지
    private String notice;           // 공지사항
    private String menuName;         // 대표 메뉴 이름
    private String menuDescription;  // 대표 메뉴 설명
    private String menuPrice;        // 대표 메뉴 가격
    private String menuImageUrl;     // 대표 메뉴 사진

    /**
     * [변환 생성자: Entity(DB 원본) -> DTO(배달 상자) 포장 기능]
     */
    public RestaurantDto(Restaurant restaurant) {
        this.restaurantId = restaurant.getId();
        this.name = restaurant.getName();
        this.category = restaurant.getCategory();
        this.tags = restaurant.getTags();
        this.rating = restaurant.getRating();
        this.reviewCount = restaurant.getReviewCount();
        this.description = restaurant.getDescription();
        this.address = restaurant.getAddress();
        this.latitude = restaurant.getLatitude();
        this.longitude = restaurant.getLongitude();
        this.openingHours = restaurant.getOpeningHours();
        this.phone = restaurant.getPhone();
        this.priceRange = restaurant.getPriceRange();
        this.websiteUrl = restaurant.getWebsiteUrl();
        this.seatInfo = restaurant.getSeatInfo();
        this.reservationInfo = restaurant.getReservationInfo();
        this.paymentMethods = restaurant.getPaymentMethods();
        this.languages = restaurant.getLanguages();
        this.mainImageUrl = restaurant.getMainImageUrl();
        this.menuName = restaurant.getMenuName();
        this.menuDescription = restaurant.getMenuDescription();
        this.menuPrice = restaurant.getMenuPrice();
        this.menuImageUrl = restaurant.getMenuImageUrl();
    }
}