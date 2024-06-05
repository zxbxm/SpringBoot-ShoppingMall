package com.tjoeun.shop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tjoeun.shop.dto.WishItemDetailDto;
import com.tjoeun.shop.entity.Item;
import com.tjoeun.shop.entity.Member;
import com.tjoeun.shop.entity.Wish;
import com.tjoeun.shop.entity.WishItem;
import com.tjoeun.shop.repository.ItemRepository;
import com.tjoeun.shop.repository.MemberRepository;
import com.tjoeun.shop.repository.WishItemRepository;
import com.tjoeun.shop.repository.WishRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WishService {
	
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final WishItemRepository wishItemRepository;
	private final WishRepository wishRepository;
	
	public Long addwishItem(String email, Long itemId) {
    Member member = memberRepository.findByEmail(email);
    if (member == null) {
        throw new EntityNotFoundException("회원정보를 찾을 수 없습니다.");
    }

    Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new EntityNotFoundException("상품정보를 찾을 수 없습니다."));

    // 회원의 찜목록 조회 또는 생성
    Wish wish = wishRepository.findByMemberId(member.getId());
    if (wish == null) {
        wish = Wish.createWish(member);
        wishRepository.save(wish);
    }

    // 찜목록 아이템 검색 또는 생성
    WishItem savedWishItem = wishItemRepository.findByWishIdAndItemId(wish.getId(), item.getId());
    if (savedWishItem != null) {
        wishItemRepository.delete(savedWishItem);
        return savedWishItem.getId();
    } else {
        WishItem wishItem = WishItem.createWishItem(wish, item, member);
        wishItemRepository.save(wishItem);
        return wishItem.getId();
    }
}


	
	//찜목록을 조회하는 코드 
	@Transactional(readOnly = true)
	public List<WishItemDetailDto> getWishList(String email){
		List<WishItemDetailDto> wishDetailDtoList = new ArrayList<>();
		
		//이메일로 사용자 정보 조회
		Member member = memberRepository.findByEmail(email);
		
		//사용자 ID를 기반으로 해당 사용자의 찜목록 조회
		Wish wish = wishRepository.findByMemberId(member.getId());
		if(wish == null) {
			return wishDetailDtoList;
		}
		wishDetailDtoList = wishItemRepository.findWishDetailDtoListByWishId(wish.getId());
		return wishDetailDtoList;
	}
	
	
	//찜한 상품 취소하기
	public void deleteWishItem(Long wishItemId) {
		WishItem wishItem = wishItemRepository.findById(wishItemId)
				.orElseThrow(EntityNotFoundException::new);
		wishItemRepository.delete(wishItem);
	}
	
	 // 상품이 찜목록에 있는지 확인하는 메소드 추가
  @Transactional(readOnly = true)
  public boolean isInWishlist(String email, Long itemId) {
      Member member = memberRepository.findByEmail(email);
      if (member == null) {
          throw new EntityNotFoundException("회원정보를 찾을 수 없습니다.");
      }

      Wish wish = wishRepository.findByMemberId(member.getId());
      if (wish == null) {
          return false;
      }

      WishItem wishItem = wishItemRepository.findByWishIdAndItemId(wish.getId(), itemId);
      return wishItem != null;
  }
  
  //찜상품 아이디 정보를 가져오는 메소드 
  public Long getWishItemId(String email, Long itemId) {
    return wishItemRepository.findWishItemIdByEmailAndItemId(email, itemId);
}
  
}
