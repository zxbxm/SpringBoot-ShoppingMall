package com.tjoeun.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "wish_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"wish_id", "item_id"})
})
@Getter @Setter
public class WishItem extends BaseEntity {
	
	/*
	 * id      : 위시 상품 코드
	 * wish_id : 위시리스트 코드 
	 * item_id : 상품 코드
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wish_item_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "wish_id")
	private Wish wish;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;	
	
	//찜목록에 담는 상품 Entity를 생성하는 메소드
	public static WishItem createWishItem(Wish wish,Item item,Member member) {
		WishItem wishItem = new WishItem();
		wishItem.setMember(member);
		wishItem.setWish(wish);
		wishItem.setItem(item);
		return wishItem;
	}
}
