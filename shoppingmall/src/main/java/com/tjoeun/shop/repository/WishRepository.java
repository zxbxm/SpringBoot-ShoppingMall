package com.tjoeun.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjoeun.shop.entity.Wish;

//찜목록 자체의 속성 & 회원과의 관계에 대한 쿼리
public interface WishRepository extends JpaRepository<Wish, Long> {
	
	//특정회원과 연결된 찜목록을 찾기 위한 메소드 	
	Wish findByMemberId(Long memberId);

}
