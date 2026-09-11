package com.bingomap.bingo_map.repository;

import com.bingomap.bingo_map.Entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Restaurant, Long>
// → "Restaurant 테이블을 다룰 건데, 기본키(id) 타입은 Long이다" 라는 뜻
// → extends(상속)만 해도 findAll(), findById(id), save() 같은 기능이 자동으로 다 생김
// → 이 안에 아무 코드도 안 써도 이미 완성된 거예요!
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}