package com.tjoeun.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tjoeun.shop.dto.WishItemDetailDto;
import com.tjoeun.shop.entity.WishItem;

//찜목록 내의 개별 아이템 & 아이템 자체에 대한 검색 관련 쿼리문 작성 
public interface WishItemRepository extends JpaRepository<WishItem, Long> {
	
	 @Query("SELECT wi.id FROM WishItem wi WHERE wi.member.email = :email AND wi.item.id = :itemId")
   Long findWishItemIdByEmailAndItemId(@Param("email") String email, @Param("itemId") Long itemId);

	
	WishItem findByWishIdAndItemId(Long wishId, Long itemId);
	
	@Query("SELECT new com.tjoeun.shop.dto.WishItemDetailDto(wi.id, i.itemNm, i.price, im.imgUrl) " +
      "FROM WishItem wi " +
      "JOIN wi.item i " +
      "JOIN ItemImg im ON im.item.id = i.id " +
      "WHERE wi.wish.id = :wishId " +
      "AND im.repimgYn = 'Y' " +
      "ORDER BY wi.regTime DESC")
List<WishItemDetailDto> findWishDetailDtoListByWishId(@Param("wishId") Long wishId);

}
