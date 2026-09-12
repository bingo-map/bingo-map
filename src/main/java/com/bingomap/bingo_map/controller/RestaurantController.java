package com.bingomap.bingo_map.controller;

import com.bingomap.bingo_map.dto.RestaurantDto;
import com.bingomap.bingo_map.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // 1. 맛집 목록 페이지
    @GetMapping({"/restaurants", "/restaurants/"})
    public String restaurantsPage(Model model) {
        List<RestaurantDto> restaurantList = restaurantService.findAll();
        model.addAttribute("restaurantList", restaurantList);

        // templates/restaurants/restaurants.html 뷰 템플릿 호출
        return "restaurants/restaurants";
    }

    // 2. 상세 페이지 (/restaurants/detail?id=5 및 /restaurants/5 대응)
    @GetMapping({"/restaurants/detail", "/restaurants/{id:[0-9]+}"})
    public String restaurantDetailPage(
            @PathVariable(value = "id", required = false) Long pathId,
            @RequestParam(value = "id", required = false) Long queryId,
            Model model) {

        // id가 없으면 기본값으로 1번을 조회하도록 방어
        Long targetId = (pathId != null) ? pathId : (queryId != null ? queryId : 1L);

        RestaurantDto restaurant = restaurantService.findById(targetId);
        model.addAttribute("restaurant", restaurant);

        // templates/restaurants/detail.html 뷰 템플릿 호출
        return "restaurants/detail";
    }
}