package com.tjoeun.shop.dto;

import org.modelmapper.ModelMapper;

import com.tjoeun.shop.entity.ReviewImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewImgDto {

	private Long id;
	private String imgName;
	private String oriImgName;
	private String imgUrl;
	private String repImgYn;
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	
	// Entity --> DTO 
	public static ReviewImgDto of(ReviewImg reviewImg) {
		return modelMapper.map(reviewImg, ReviewImgDto.class);		
	}
}
