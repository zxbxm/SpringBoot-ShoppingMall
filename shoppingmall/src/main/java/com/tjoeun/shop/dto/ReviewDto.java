package com.tjoeun.shop.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.tjoeun.shop.entity.Item;
import com.tjoeun.shop.entity.Member;
import com.tjoeun.shop.entity.Review;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDto {
	
	private Long id;
	
	@NotBlank
	private String content;
	
	private int rating;
	
	private Member Member;
	
	private Item item;
	
	/*
	private List<ReviewImgDto> reviewImgDtoList = new ArrayList<>();
	*/
	
	/*
	// 리뷰 삭제 및 수정시 필요 
	private List<Long> reviewImgIds = new ArrayList<>();
	*/
	
  private static ModelMapper modelMapper = new ModelMapper();
  
  //DTO --> Entity : ModelMapper 를 사용한 code
  public Review toEntity() {
  	Review review = modelMapper.map(this, Review.class);
  	return review;
  }
  // Entity --> Dto
  public static ReviewDto toDto(Review review) {
  	ReviewDto reviewDto = modelMapper.map(review, ReviewDto.class);
  	return reviewDto;
  }
}
