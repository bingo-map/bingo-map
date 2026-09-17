//장준환
package com.bingomap.bingo_map.controller;

import com.bingomap.bingo_map.dto.RestaurantDto;
import com.bingomap.bingo_map.service.RestaurantService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//장준환
@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * 주변 맛집 목록
     * URL: /restaurants
     *
     * 실제 파일:
     * static/restaurants/restaurants.html
     */
    @GetMapping({"", "/"})
    public String restaurantsPage(Model model) {

        List<RestaurantDto> restaurantList =
                restaurantService.findAll();

        model.addAttribute("restaurantList", restaurantList);

        return "restaurants/restaurants";
    }

    /**
     * 주변 맛집 상세
     *
     * URL:
     * /restaurants/detail?id=5
     * 또는
     * /restaurants/5
     *
     * 실제 파일:
     * static/restaurants/detail.html
     */
    @GetMapping({"/detail", "/{id:[0-9]+}"})
    public String restaurantDetailPage(
            @PathVariable(value = "id", required = false) Long pathId,
            @RequestParam(value = "id", required = false) Long queryId,
            Model model) {

        Long targetId =
                (pathId != null)
                        ? pathId
                        : (queryId != null ? queryId : 1L);

        RestaurantDto restaurant =
                restaurantService.findById(targetId);

        model.addAttribute("restaurant", restaurant);

        return "restaurants/detail";
    }
}