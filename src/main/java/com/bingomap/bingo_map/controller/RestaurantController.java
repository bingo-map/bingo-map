package com.bingomap.bingo_map.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestaurantController {

    // 1. 맛집 목록 페이지
    @GetMapping({"/restaurants", "/restaurants/"})
    public String restaurantsPage() {
        return "forward:/restaurants/restaurants.html";
    }

    // 2. 상세 페이지 (/restaurants/detail 및 /restaurants/{id} 모두 대응)
    @GetMapping({"/restaurants/detail", "/restaurants/{id:[0-9]+}"})
    public String restaurantDetailPage() {
        return "forward:/restaurants/detail.html";
    }
}