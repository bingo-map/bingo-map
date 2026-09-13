package com.bingomap.bingo_map.Entity;

// JPA(Java Persistence API: 자바 객체와 데이터베이스 테이블을 이어주는 표준 도구) 관련 기능 불러오기
import jakarta.persistence.*;
// 롬복(Lombok: 코드를 줄여주는 도구로, 자동으로 get 메서드와 기본 생성자를 만들어 줌) 불러오기
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [식당 정보 엔티티 클래스]
 * - @Entity: 이 자바 클래스가 오라클 DB의 한 개 테이블과 1:1로 짝지어진다는 것을 스프링에게 알려줍니다.
 * - @Table(name = "RESTAURANT"): 오라클 DB 안에 실제로 만들어둔 'RESTAURANT' 테이블과 연결합니다.
 * - @Getter: 클래스 안의 변수값들을 밖에서 꺼내 쓸 수 있도록 getName(), getId() 등의 함수를 자동 생성합니다.
 * - @NoArgsConstructor: 파라미터(재료)가 없는 기본 생성자 'public Restaurant() {}'를 자동으로 만들어줍니다.
 */
@Entity
@Table(name = "RESTAURANT")
@Getter
@NoArgsConstructor
public class Restaurant {

    /**
     * [식당 고유 식별 번호 (PK: 기본키)]
     * - @Id: 테이블의 대표 주민번호 역할을 하는 고유 번호(Primary Key)임을 나타냅니다.
     * - @GeneratedValue / @SequenceGenerator: 오라클 시퀀스('SEQ_RESTAURANT')를 써서 1, 2, 3... 숫자를 자동으로 매겨줍니다.
     * - @Column(name = "RESTAURANT_ID"): 오라클 테이블의 'RESTAURANT_ID' 컬럼(열)과 짝을 맞춥니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_restaurant")
    @SequenceGenerator(name = "seq_restaurant", sequenceName = "SEQ_RESTAURANT", allocationSize = 1)
    @Column(name = "RESTAURANT_ID")
    private Long id;

    // [식당 이름] nullable = false는 빈 칸(NULL)으로 둘 수 없는 필수값임을 뜻합니다.
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    // [음식 분류] 타코야끼, 야끼소바 등 목록에서 필터링할 때 기준이 되는 카테고리입니다.
    @Column(name = "CATEGORY", length = 50)
    private String category;

    // [검색용 태그] '도톤보리,오사카맛집' 처럼 뱃지나 검색 키워드로 쓸 단어들입니다.
    @Column(name = "TAGS", length = 200)
    private String tags;

    // [별점 평점] 소수점이 있는 평점(예: 4.6, 4.4)을 담기 위해 Double 타입을 씁니다.
    @Column(name = "RATING")
    private Double rating;

    // [리뷰 총 개수] 등록된 후기 개수를 정수(숫자) 형태로 담습니다.
    @Column(name = "REVIEW_COUNT")
    private Integer reviewCount;

    // [식당 한 줄 소개글] 카드나 상세 페이지 상단에 들어갈 매장 설명글입니다.
    @Column(name = "DESCRIPTION", length = 1000)
    private String description;

    // [매장 주소] 실제 식당이 위치한 도로명/지번 주소 텍스트입니다.
    @Column(name = "ADDRESS", length = 300)
    private String address;

    // [위도 좌표] 지도에서 세로 위치를 찍어줄 위도(Latitude) 숫자값입니다. (예: 34.668729)
    @Column(name = "LATITUDE")
    private Double latitude;

    // [경도 좌표] 지도에서 가로 위치를 찍어줄 경도(Longitude) 숫자값입니다. (예: 135.501294)
    @Column(name = "LONGITUDE")
    private Double longitude;

    // [영업 시간] 오픈/마감 시간 정보입니다. (예: '09:00 - 21:00')
    @Column(name = "OPENING_HOURS", length = 100)
    private String openingHours;

    // [전화번호] 매장 대표 번호이며, 상세페이지에서 누르면 바로 전화가 걸리는 링크로 연동됩니다.
    @Column(name = "PHONE", length = 50)
    private String phone;

    // [예상 가격대] 1인당 평균 예산 범위를 나타냅니다. (예: '¥500 - ¥1,500')
    @Column(name = "PRICE_RANGE", length = 50)
    private String priceRange;

    // [공식 웹사이트 URL] 매장 공식 홈페이지나 SNS 인스타그램 인터넷 주소입니다.
    @Column(name = "WEBSITE_URL", length = 300)
    private String websiteUrl;

    // [좌석 정보] 식당 내부 좌석 형태입니다. (예: '20석', '테이크아웃 전용')
    @Column(name = "SEAT_INFO", length = 100)
    private String seatInfo;

    // [예약 안내] 예약이 가능한지 여부입니다. (예: '예약 불가 (현장 대기)')
    @Column(name = "RESERVATION_INFO", length = 100)
    private String reservationInfo;

    // [결제 수단] 결제 가능한 결제 방식입니다. (예: '현금, 신용카드, 전자화폐')
    @Column(name = "PAYMENT_METHODS", length = 200)
    private String paymentMethods;

    // [지원 언어] 매장에 구비된 메뉴판 언어입니다. (예: '일본어, 한국어 메뉴판')
    @Column(name = "LANGUAGES", length = 200)
    private String languages;

    // [대표 이미지 경로] static 폴더 안의 사진 파일 주소입니다. (예: '/images/food-takoyaki.png')
    @Column(name = "MAIN_IMAGE_URL", length = 500)
    private String mainImageUrl;
}