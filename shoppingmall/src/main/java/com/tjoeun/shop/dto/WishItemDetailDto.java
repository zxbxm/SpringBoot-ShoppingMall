package com.tjoeun.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
//찜목록 내 개별 상품 DTO 
public class WishItemDetailDto {
	
	private Long wishItemId; //찜목록 상품 아이디
	private String itemNm;// 상품명
	private int price;//상품금액
	private String imgUrl;//상품 이미지 경로
	
	//다른 로직에서 찜상품에 대한 정보를 사용하기 위해 생성자를 통해 정보를 저장
	public WishItemDetailDto(Long wishItemId, String itemNm, int price, String imgUrl) {
		this.wishItemId = wishItemId;
		this.itemNm =itemNm;
		this.price = price;
		this.imgUrl = imgUrl;
	}
}











// 추후 찜목록 내 상품에 대한 기능을 추가할 때 생성자가 유용하게 쓰임 