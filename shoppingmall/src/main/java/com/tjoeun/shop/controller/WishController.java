package com.tjoeun.shop.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tjoeun.shop.dto.WishItemDetailDto;
import com.tjoeun.shop.service.WishService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/wish")
public class WishController {
	
	private final WishService wishService;
	
  // 찜목록에 아이템 추가
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<?> addWishItem(@RequestBody Map<String, Long> params, Principal principal) {
	    if (principal == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증이 필요합니다.");
	    }

	    Long itemId = params.get("itemId");
	    if (itemId == null) {
	        return ResponseEntity.badRequest().body("itemId는 필수입니다.");
	    }

	    try {
	        String email = principal.getName(); // Principal 객체에서 이메일(사용자 ID)을 가져옵니다.
	        Long wishItemId = wishService.addwishItem(email, itemId);
	        return ResponseEntity.ok(Collections.singletonMap("wishItemId", wishItemId)); // wishItemId 반환
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (NumberFormatException e) {
	        return ResponseEntity.badRequest().body("잘못된 숫자 형식입니다.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}



  // 찜목록 조회
  @GetMapping
  public ResponseEntity<List<WishItemDetailDto>> getWishList(@AuthenticationPrincipal UserDetails userDetails) {
      String email = userDetails.getUsername(); // UserDetails를 통해 이메일 가져오기
      List<WishItemDetailDto> wishList = wishService.getWishList(email);
      if (wishList.isEmpty()) {
          return ResponseEntity.noContent().build();
      }
      return ResponseEntity.ok(wishList);
  }

  // 찜목록에서 아이템 제거 - 찜목록 페이지에서 작동
  @DeleteMapping("/{wishItemId}")
  public ResponseEntity<?> removeWishItem(@PathVariable Long wishItemId) {
      try {
          wishService.deleteWishItem(wishItemId);
          return ResponseEntity.ok().build();
      } catch (EntityNotFoundException e) {
          return ResponseEntity.notFound().build();
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
      }
  }
  
  
  
  @GetMapping("/wishList")
  public String showWishlist(@AuthenticationPrincipal UserDetails userDetails, Model model) {
      if (userDetails != null) {
          String username = userDetails.getUsername(); // 사용자 이름 가져오기
          List<WishItemDetailDto> wishItemDetailList = wishService.getWishList(username);
          model.addAttribute("wishItems", wishItemDetailList);
          return "/wish/wishList"; // Thymeleaf 템플릿의 경로가 맞는지 확인하세요.
      } else {
          return "redirect:/login"; // 사용자가 인증되지 않았을 경우 로그인 페이지로 리다이렉트
      }
  }
  
  @PostMapping("/check")
  public ResponseEntity<Map<String, Object>> checkWishlistStatus(@RequestBody Map<String, Long> request, Principal principal) {
      Long itemId = request.get("itemId");
      // 현재 로그인된 사용자의 이메일을 가져오는 방식
      String email = principal.getName();

      boolean inWishlist = wishService.isInWishlist(email, itemId);
      Long wishItemId = null;
      if (inWishlist) {
          wishItemId = wishService.getWishItemId(email, itemId);
      }
      Map<String, Object> response = new HashMap<>();
      response.put("inWishlist", inWishlist);
      response.put("wishItemId", wishItemId);
      
      return ResponseEntity.ok(response);
  }
}
