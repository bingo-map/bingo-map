package com.bingomap.bingo_map.service;

import com.bingomap.bingo_map.dto.RestaurantDto;
import com.bingomap.bingo_map.Entity.Restaurant;
import com.bingomap.bingo_map.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// @Service: "이 클래스는 서비스(중간 담당자) 역할이다" 라는 표시.
// 이게 있어야 Controller에서 이 클래스를 갖다 쓸 수 있어요.
@Service
public class RestaurantService {

    // Repository(3번에서 만든 창구)를 가져다 쓰기 위해 필드로 선언
    private final RestaurantRepository restaurantRepository;

    // 생성자: Spring이 시작할 때 RestaurantRepository를 자동으로 넣어줌
    // (이런 방식을 "의존성 주입"이라고 부르는데, 지금은 "그냥 이렇게 쓴다" 정도만 알아도 돼요)
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // 기능 1: 맛집 전체 목록 가져오기
    public List<RestaurantDto> findAll() {
        // 1) Repository한테 "DB에 있는 전체 데이터 다 줘" 라고 요청 (Entity 목록으로 옴)
        List<Restaurant> restaurantList = restaurantRepository.findAll();

        // 2) Entity 목록을 하나씩 DTO로 바꿔서 새 목록으로 만듦
        return restaurantList.stream()
                .map(this::toDto)   // 아래에 있는 toDto() 함수를 하나하나 적용
                .collect(Collectors.toList());
    }

    // 기능 2: id 하나로 맛집 상세 정보 가져오기
    public RestaurantDto findById(Long id) {
        // 1) Repository한테 "이 id 데이터 줘" 라고 요청
        Restaurant restaurant = restaurantRepository.findById(id)
                // 만약 그 id가 DB에 없으면 에러를 발생시킴 (없는 맛집 조회 방지)
                .orElseThrow(() -> new IllegalArgumentException("해당 맛집이 없습니다. id=" + id));

        // 2) Entity를 DTO로 바꿔서 리턴
        return toDto(restaurant);
    }

    // Entity → DTO로 값을 옮겨 담는 변환 함수 (여기서만 쓰는 내부용이라 private)
    private RestaurantDto toDto(Restaurant r) {
        RestaurantDto dto = new RestaurantDto();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setCategory(r.getCategory());
        dto.setTags(r.getTags());
        dto.setRating(r.getRating());
        dto.setReviewCount(r.getReviewCount());
        dto.setDescription(r.getDescription());
        dto.setAddress(r.getAddress());
        dto.setLatitude(r.getLatitude());
        dto.setLongitude(r.getLongitude());
        dto.setOpeningHours(r.getOpeningHours());
        dto.setPhone(r.getPhone());
        dto.setPriceRange(r.getPriceRange());
        dto.setWebsiteUrl(r.getWebsiteUrl());
        dto.setSeatInfo(r.getSeatInfo());
        dto.setReservationInfo(r.getReservationInfo());
        dto.setPaymentMethods(r.getPaymentMethods());
        dto.setLanguages(r.getLanguages());
        dto.setMainImageUrl(r.getMainImageUrl());
        return dto;
    }
}