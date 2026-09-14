package com.bingomap.bingo_map.dto;

// 1번에서 만든 Restaurant 엔티티(Entity: DB 원본 데이터 모델) 클래스를 불러옵니다[cite: 1].
import com.bingomap.bingo_map.Entity.Restaurant;
// 롬복(Lombok: 반복되는 자바 코드를 자동으로 만들어주는 편리한 도구) 어노테이션 불러오기
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [식당 정보 DTO(Data Transfer Object) 클래스]
 * - DTO란?: "데이터 전송 객체"라는 뜻으로, DB 테이블 원본(엔티티)을 화면에 그대로 노출하지 않고
 *            화면(HTML)에 딱 필요한 데이터만 안전하게 담아 나르는 '배달용 선물 상자' 역할을 합니다.
 * - @Getter: 상자 안에 담긴 데이터(가게 이름, 주소 등)를 꺼내 쓸 수 있는 get 함수들을 자동으로 만듭니다.
 * - @Setter: 상자 안에 새로운 데이터를 넣을 수 있는 set 함수들을 자동으로 만듭니다.
 * - @NoArgsConstructor: 아무런 재료 없이 빈 상자(기본 생성자)를 만드는 코드 'public RestaurantDto() {}'를 자동 생성합니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class RestaurantDto {

    // [식당 고유 번호] 상세 페이지로 이동할 때 식당을 구분하는 번호표(ID)입니다.
    private Long id;

    // [식당 이름] 화면 카드와 상세 화면 상단에 굵게 찍힐 매장 상호명입니다.
    private String name;

    // [음식 카테고리] 타코야끼, 야끼소바 등 목록 필터링에 사용할 음식 종류입니다[cite: 1, 3].
    private String category;

    // [검색 키워드 태그] '도톤보리,오사카맛집' 등 검색과 뱃지 표시에 쓸 단어들입니다[cite: 3].
    private String tags;

    // [별점 평점] 소수점 한 자리 평점(예: 4.6, 4.4)을 담는 실수형(Double) 변수입니다[cite: 3].
    private Double rating;

    // [리뷰 개수] 등록된 총 방문 후기 숫자를 담는 정수형(Integer) 변수입니다[cite: 3].
    private Integer reviewCount;

    // [한 줄 소개글] 카드 설명 및 매장 소개 문구로 사용할 텍스트입니다[cite: 1, 3].
    private String description;

    // [식당 주소] 손님들이 찾아올 수 있는 실제 도로명/지번 주소입니다[cite: 1, 3].
    private String address;

    // [위도 좌표] 지도에서 세로 위치를 지정하는 위도값입니다 (예: 34.668729)[cite: 3].
    private Double latitude;

    // [경도 좌표] 지도에서 가로 위치를 지정하는 경도값입니다 (예: 135.501294)[cite: 3].
    private Double longitude;

    // [영업 시간] 오픈 및 마감 시간 텍스트입니다 (예: '09:00 - 21:00')[cite: 3].
    private String openingHours;

    // [전화번호] 매장 번호이며, 상세페이지에서 누르면 전화가 걸리는 링크로 연결됩니다[cite: 3].
    private String phone;

    // [예상 가격대] 1인당 평균 예산 범위 텍스트입니다 (예: '¥500 - ¥1,500')[cite: 3].
    private String priceRange;

    // [공식 홈페이지 URL] 식당 공식 웹사이트나 인스타그램 인터넷 주소입니다[cite: 3].
    private String websiteUrl;

    // [좌석 정보] 매장 내부 좌석 형태입니다 (예: '20석', '테이크아웃 전용')[cite: 3].
    private String seatInfo;

    // [예약 안내] 방문 전 예약 가능 여부입니다 (예: '예약 불가 (현장 대기)')[cite: 3].
    private String reservationInfo;

    // [결제 수단] 현장 결제 방식입니다 (예: '현금, 신용카드, 전자화폐')[cite: 3].
    private String paymentMethods;

    // [지원 언어] 매장에 구비된 메뉴판 언어입니다 (예: '일본어, 한국어 메뉴판')[cite: 3].
    private String languages;

    // [대표 사진 경로] static 폴더 안의 이미지 파일 위치 주소입니다 (예: '/images/food-takoyaki.png')[cite: 1, 3].
    private String mainImageUrl;

    /**
     * [변환 생성자: Entity(DB 원본) -> DTO(배달 상자) 포장 기능]
     * - Service(서비스 계층)에서 DB에서 꺼낸 Restaurant 객체를 이 생성자에 쏙 넣어주면,[cite: 1, 3]
     *   모든 필드값을 자동으로 꺼내서 배달 상자(DTO)에 1초 만에 깔끔하게 채워 넣습니다[cite: 1, 3].
     */
    public RestaurantDto(Restaurant restaurant) {
        this.id = restaurant.getId();
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
    }
}