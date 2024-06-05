package com.tjoeun.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
imgName : (저장되는) 이미지 파일 이름
oriImgName : (원본) 이미지 파일 이름
imgUrl : 이미지 파일 경로
repImgYn : 대표 이미지인지 아닌지... String
*/

@Entity
@Getter @Setter @ToString
public class ReviewImg extends BaseEntity {

	@Id
	@Column(name="review_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName;
	private String oriImgName;
	private String imgUrl;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;
	
	public void updateReviewImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
	
}
