package com.bingomap.bingo_map.Entity;

import jakarta.persistence.*; // DB랑 자바를 연결해주는 도구 모음 (JPA)
import lombok.Getter;
import lombok.Setter;

// @Entity: "이 클래스는 DB 테이블 하나랑 짝지어진 클래스다" 라는 표시
@Entity
// @Table: 짝지어질 테이블 이름을 알려줌 (RESTAURANT 테이블)
@Table(name = "RESTAURANT")
@Getter  // 변수마다 getXxx() 함수를 자동으로 만들어줌 (Lombok 기능)
@Setter  // 변수마다 setXxx() 함수를 자동으로 만들어줌 (Lombok 기능)
public class Restaurant {

    // @Id: 이 변수가 테이블의 기본키(PK, RESTAURANT_ID)라는 표시
    @Id
    // @GeneratedValue + @SequenceGenerator: 저장할 때 SEQ_RESTAURANT 시퀀스로 번호를 자동 채번
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_seq")
    @SequenceGenerator(name = "restaurant_seq", sequenceName = "SEQ_RESTAURANT", allocationSize = 1)
    @Column(name = "RESTAURANT_ID")
    private Long id;

    // @Column: 이 변수가 테이블의 어떤 컬럼과 연결되는지 이름을 지정
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TAGS")
    private String tags;

    @Column(name = "RATING")
    private Double rating;

    @Column(name = "REVIEW_COUNT")
    private Integer reviewCount;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "LATITUDE")
    private Double latitude;

    @Column(name = "LONGITUDE")
    private Double longitude;

    @Column(name = "OPENING_HOURS")
    private String openingHours;

    @Column(name = "PHONE")
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

    @Column(name = "LANGUAGES")
    private String languages;

    @Column(name = "MAIN_IMAGE_URL")
    private String mainImageUrl;

    // 기본 생성자 (JPA는 이게 꼭 있어야 함, 비워둬도 됨)
    protected Restaurant() {}
}