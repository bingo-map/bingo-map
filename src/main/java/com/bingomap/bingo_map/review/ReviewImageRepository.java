package com.bingomap.bingo_map.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

    List<ReviewImage> findByReview_ReviewIdOrderBySortOrderAsc(Long reviewId);
}
