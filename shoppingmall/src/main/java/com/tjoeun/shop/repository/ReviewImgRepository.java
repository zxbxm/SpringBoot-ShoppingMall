package com.tjoeun.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjoeun.shop.entity.ReviewImg;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {

	List<ReviewImg> findByreviewIdOrderByIdAsc(Long reviewId);
	
}
