package com.bingomap.bingo_map.controller;

// DTO(화면에 전달할 데이터 상자)와 Service(DB 비즈니스 로직 처리기) 불러오기[cite: 1, 3]
import com.bingomap.bingo_map.dto.RestaurantDto;
import com.bingomap.bingo_map.service.RestaurantService;

// 스프링 MVC(웹 요청을 받아 처리하는 웹 프레임워크) 관련 도구 불러오기
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * [식당 화면 제어 컨트롤러(Controller) 클래스]
 * - @Controller: 사용자가 브라우저 주소창에 URL을 입력했을 때, 요청을 가장 먼저 받아 처리하고
 *   알맞은 HTML 화면(View)을 띄워주는 "안내 데스크(교통정리 담당)" 역할을 합니다.
 */
@Controller
public class RestaurantController {

    // 실제 데이터베이스 조회 업무를 맡길 Service(서비스 객체) 연결 통로
    private final RestaurantService restaurantService;

    /**
     * [생성자 주입 (DI: 의존성 주입)]
     * 스프링 부트가 실행될 때 만들어 둔 RestaurantService 객체를 컨트롤러에 자동으로 넣어줍니다.
     */
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * 1. [맛집 목록 전체 페이지]
     * - @GetMapping: 사용자가 브라우저에서 'GET' 방식(일반적인 웹페이지 접속)으로 주소를 요청했을 때 연결합니다.
     * - {"/restaurants", "/restaurants/"}: 주소 끝에 슬래시(/)가 붙든 안 붙든 둘 다 이 함수가 처리합니다.
     * - Model(배달 바구니): 자바에서 꺼낸 DB 데이터를 HTML 템플릿(Thymeleaf)으로 넘겨줄 때 사용하는 전달 상자입니다.
     */
    

    @GetMapping({"/restaurants", "/restaurants/"})
    public String restaurantsPage(Model model) {
        // 1) Service에게 부탁하여 오라클 DB의 모든 맛집 데이터를 DTO 리스트 형태로 가져옵니다.
        List<RestaurantDto> restaurantList = restaurantService.findAll();

        // 2) HTML 화면에서 사용할 수 있도록 "restaurantList"라는 이름표를 붙여 바구니(model)에 담습니다[cite: 1].
        model.addAttribute("restaurantList", restaurantList);

        // 3) templates/restaurants/restaurants.html 파일을 찾아 화면을 브라우저에 그려줍니다[cite: 1].
        return "restaurants/restaurants";
    }

    /**
     * 2. [맛집 상세 페이지]
     * 두 가지 주소 형식(/restaurants/detail?id=5 또는 /restaurants/5)을 모두 처리할 수 있도록 설계되었습니다[cite: 1].
     *
     * - @PathVariable(경로 변수): URL 주소 경로 속에 포함된 숫자(/restaurants/5 의 '5')를 꺼내옵니다.
     * - @RequestParam(쿼리 스트링): 물음표 뒤에 붙은 파라미터(/restaurants/detail?id=5 의 '5')를 꺼내옵니다.
     * - required = false: 번호가 안 넘어와도 에러를 내며 멈추지 않고 일단 코드를 계속 실행하게 해줍니다.
     */
    @GetMapping({"/restaurants/detail", "/restaurants/{id:[0-9]+}"})
    public String restaurantDetailPage(
            @PathVariable(value = "id", required = false) Long pathId,
            @RequestParam(value = "id", required = false) Long queryId,
            Model model) {

        // [방어 코드]: 주소창에 번호를 안 적고 들어왔을 경우(예: 그냥 /restaurants/detail 접속)
        // 에러 창(500 에러)이 뜨지 않고 기본으로 1번 식당 데이터를 보여주도록 안전장치를 겁니다.
        Long targetId = (pathId != null) ? pathId : (queryId != null ? queryId : 1L);

        // 1) 찾아낸 식당 번호(targetId)를 전달해 DB에서 해당 식당 1건의 상세 정보를 가져옵니다[cite: 1].
        RestaurantDto restaurant = restaurantService.findById(targetId);

        // 2) HTML 화면에 쓸 수 있도록 "restaurant"라는 이름표로 바구니(model)에 담습니다[cite: 1].
        model.addAttribute("restaurant", restaurant);

        // 3) templates/restaurants/detail.html 파일을 열어 화면을 브라우저에 띄웁니다[cite: 1].
        return "restaurants/detail";
    }
}