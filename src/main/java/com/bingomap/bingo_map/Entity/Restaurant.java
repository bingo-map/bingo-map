package com.bingomap.bingo_map.Entity; // 패키지명은 본인 프로젝트에 맞게

import jakarta.persistence.*;
import lombok.Getter; // 1. 롬복 Getter 불러오기
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RESTAURANT")
@Getter // 2. 여기에 @Getter가 반드시 붙어 있어야 합니다!
@Setter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESTAURANT_GEN")
    @SequenceGenerator(name = "SEQ_RESTAURANT_GEN", sequenceName = "SEQ_RESTAURANT", allocationSize = 1)
    @Column(name = "RESTAURANT_ID")
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

    @Column(name = "OPENING_HOURS")
    private String openingHours;

    private String phone;

    @Column(name = "PRICE_RANGE")
    private String priceRange;

    @Column(name = "WEBSITE_URL")
    private String websiteUrl;

    @Column(name = "SEAT_INFO")
    private String seatInfo;

    @Column(name = "RESERVATION_INFO")
    private String reservationInfo;

    @Column(name = "PAYMENT_METHODS")
    private String paymentMethods;

    private String languages;

    @Column(name = "MAIN_IMAGE_URL")
    private String mainImageUrl;

    // ▼ 3. 이 필드들이 엔티티에도 선언되어 있어야 합니다.

    @Column(name = "MENU_NAME")
    private String menuName;

    @Column(name = "MENU_DESCRIPTION")
    private String menuDescription;

    @Column(name = "MENU_PRICE")
    private String menuPrice;

    @Column(name = "MENU_IMAGE_URL")
    private String menuImageUrl;
}