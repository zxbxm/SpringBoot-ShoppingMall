package com.tjoeun.shop.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@Table(name="wish")
@ToString(exclude = "wishDate")
public class Wish extends BaseEntity {
	
	@Id @GeneratedValue
	@Column(name = "wish_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="member_id")
	private Member member;
	
	private LocalDateTime wishDate;
	
	// 삭제하기 위해 추가
	 @OneToMany(mappedBy="wish", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<WishItem> wishItems;
	 
	 //찜한 상품들을 찜목록List에 추가하기
	 //wish 와 wishItem 의 양방향 데이터전달의 중요한 메소드
	 public void addWishItem(WishItem wishItem) {
		 this.wishItems.add(wishItem); //wish의 wishlist 안에 wishItem 을 추가하기 
		 wishItem.setWish(this); //wishList에 들어간 찜 상품의 찜목록 객체를 설정해주는 코드
		 
	 }
	 
	 //Wish 객체 생성 및 찜목록 만드는 메소드
	 public static Wish createWish(Member member) {
		 Wish wish = new Wish();
		 wish.setMember(member);
		 /*
		 for(WishItem wishItem :wishItemList) {
			wish.addWishItem(wishItem);
		 }
		 wish.setWishDate(LocalDateTime.now());
		 */
		 return wish;
		 
	 }
	 
	 //찜 취소하기
	 
	 


}
