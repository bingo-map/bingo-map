package com.bingomap.bingo_map.service;

import com.bingomap.bingo_map.dto.RestaurantDto;
import com.bingomap.bingo_map.Entity.Restaurant;
import com.bingomap.bingo_map.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // 기능 1: 맛집 전체 목록 가져오기
    public List<RestaurantDto> findAll() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();

        return restaurantList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 기능 2: id 하나로 맛집 상세 정보 가져오기 (null 방어 로직)
    public RestaurantDto findById(Long id) {
        if (id == null) {
            id = 1L;
        }

        Long targetId = id;
        Restaurant restaurant = restaurantRepository.findById(targetId)
                .orElseGet(() -> restaurantRepository.findAll().stream().findFirst().orElse(null));

        if (restaurant == null) {
            return null;
        }

        return toDto(restaurant);
    }

    // Entity → DTO 변환 함수
    private RestaurantDto toDto(Restaurant r) {
        RestaurantDto dto = new RestaurantDto();
        dto.setRestaurantId(r.getId());
        dto.setName(r.getName());
        dto.setCategory(r.getCategory());
        dto.setTags(r.getTags());
        dto.setRating(r.getRating() != null ? r.getRating() : 0.0);
        dto.setReviewCount(r.getReviewCount() != null ? r.getReviewCount() : 0);
        dto.setDescription(r.getDescription());
        dto.setAddress(r.getAddress());

        // Entity와 DTO의 실제 필드명에 맞게 매핑 (latitude, longitude)
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
        dto.setMenuName(r.getMenuName());
        dto.setMenuPrice(r.getMenuPrice());
        dto.setMenuDescription(r.getMenuDescription());
        dto.setMenuImageUrl(r.getMenuImageUrl());


        return dto;
    }
}