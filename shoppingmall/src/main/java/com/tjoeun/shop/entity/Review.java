package com.tjoeun.shop.entity;

import java.util.ArrayList;
import java.util.List;

import com.tjoeun.shop.dto.ItemFormDto;
import com.tjoeun.shop.dto.ReviewDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Review extends BaseEntity {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long id;
	
	private String content;
	
	private int rating;
	
	
	//지연 로딩: Review 엔티티의 member 필드는 지연 로딩으로 설정되어 있어,
	//실제로 접근할 때 데이터베이스에서 조회합니다.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

  //리뷰 삭제 및 수정시 필요 
	@OneToMany(mappedBy = "review", orphanRemoval = true,
			cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReviewImg> reviewImgList = new ArrayList<>();
	
	public void updateReview(ReviewDto reviewDto){
    this.content = reviewDto.getContent();
    
}
	
}
