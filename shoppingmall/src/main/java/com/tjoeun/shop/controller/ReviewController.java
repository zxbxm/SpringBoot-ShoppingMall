package com.tjoeun.shop.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tjoeun.shop.dto.ItemFormDto;
import com.tjoeun.shop.dto.ReviewDto;
import com.tjoeun.shop.entity.Item;
import com.tjoeun.shop.entity.Member;
import com.tjoeun.shop.service.ItemService;
import com.tjoeun.shop.service.MemberService;
import com.tjoeun.shop.service.ReviewImgService;
import com.tjoeun.shop.service.ReviewService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

	private final ReviewService reviewService;
	private final MemberService memberService;
	private final ItemService itemService;
	private final ReviewImgService reviewImgService;
	
	@PostMapping("/review/item")
	//@ResponseBody
	public String saveReview(@Valid ReviewDto reviewDto, BindingResult bindingResult, @RequestParam("itemId") long itemId, Principal principal,
			                     Model model,@RequestParam("reviewImgFile") List<MultipartFile> reviewImgFile
													) {
		
		
	  if(bindingResult.hasErrors()) {
	  	return "redirect:/review/" + itemId;
	  	}
	  
	   String email = principal.getName();
	  
	   Member member = memberService.findMember(email);
	   Item item     = itemService.findItem(itemId)
	  											     .orElseThrow(EntityNotFoundException::new);
	  
     reviewDto.setItem(item);
     reviewDto.setMember(member);
     
     
     try {
			reviewService.saveReview(reviewDto, reviewImgFile);
		} catch (Exception e) {
	    model.addAttribute("errorMessage", "상품 등록 중 오류가 발생함 !!!");
	    return "redirect:/review/" + itemId;
		}
     
     
     
     return "redirect:/review/" + itemId;
	}
	
	@GetMapping("/review/{itemId}")
	public String getItemDetail(@PathVariable("itemId") long itemId, Model model, ReviewDto reviewDto, Principal principal) {
	    // itemId에 해당하는 아이템 상세 정보를 조회하고 모델에 추가합니다.
	    ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
	    
	    
	    model.addAttribute("item", itemFormDto);
	    model.addAttribute("itemId", itemId);
	    model.addAttribute("userEmail", principal.getName());
	    model.addAttribute("reviewList", reviewService.getReviewsByItemId(itemId));
	    
	    return "item/itemDtl"; // 아이템 상세 페이지로 리다이렉트합니다.
	}
	
	
	@PostMapping("/review/update")
	@ResponseBody
	public ResponseEntity<String> updateReview(@RequestBody ReviewDto reviewDto,
																							Principal principal, Model model){
		
		log.info(">>>>>>>>>>>>>>>> reviewDto" + reviewDto);
		log.info(">>>>>>>>>>>>>>>> getID" + reviewDto.getId());
		log.info(">>>>>>>>>>>>>>>> getContent" + reviewDto.getContent());
		log.info(">>>>>>>>>>>>>>>> test");
		
		
		try {
			reviewService.updateReview(reviewDto);
			return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 수정 중 오류가 발행했습니다.");
		}
		
	}
	
	@PostMapping("/review/update/image/{reviewImgId}")
  @ResponseBody
  public ResponseEntity<String> updateReviewImage(@PathVariable Long reviewImgId,
                                  @RequestParam("reviewImgFile") MultipartFile reviewImgFile) {
     
		log.info(">>>>>>>>>>>>>>>> reviewId" + reviewImgId);
		log.info(">>>>>>>>>>>>>>>> reviewImgFile" + reviewImgFile);
		
		
		try {
			reviewImgService.updateItemImg(reviewImgId, reviewImgFile);
			return ResponseEntity.ok("리뷰이미지가 성공적으로 수정되었습니다.");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰이미지 수정 중 오류가 발행했습니다.");
		}
      
  }
	
	@PostMapping("/review/delete/{reviewId}")
	@ResponseBody
	public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
	    try {
	        reviewService.deleteReview(reviewId);
	        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
	    } catch(Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 중 오류가 발생했습니다.");
	    }
	}
	
	
}
