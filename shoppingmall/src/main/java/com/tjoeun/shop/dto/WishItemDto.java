package com.tjoeun.shop.dto;

import jakarta.validation.constraints.NotNull;

//상품을 찜목록에 추가할 때 사용하는 Dto
public class WishItemDto {

	@NotNull(message = "상품 아이디를 입력해주세요")
	private Long wishId;
}
