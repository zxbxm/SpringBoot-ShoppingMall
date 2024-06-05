package com.tjoeun.shop.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjoeun.shop.entity.Review;

public interface ReviewRepository extends JpaRepository <Review, Long> {

	List<Review> findByItemId(Long itemId);
}
