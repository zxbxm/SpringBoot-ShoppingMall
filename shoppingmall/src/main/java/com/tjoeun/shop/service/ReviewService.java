package com.tjoeun.shop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tjoeun.shop.dto.ReviewDto;
import com.tjoeun.shop.entity.Review;
import com.tjoeun.shop.entity.ReviewImg;
import com.tjoeun.shop.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
	
	@Value("${reviewImgLocation}")
	private String reviewImgLocation;

	private final ReviewRepository reviewRepository;
	private final ReviewImgService reviewImgService;
	private final FileService fileService;
	
	public Long saveReview(ReviewDto reviewDto, List<MultipartFile> reviewImgFileList)throws Exception {
		
		Review review = reviewDto.toEntity();
		reviewRepository.save(review);
		
		for(int i=0; i<reviewImgFileList.size(); i++) {
			ReviewImg reviewImg = new ReviewImg();
			reviewImg.setReview(review);

			reviewImgService.saveReviewImg(reviewImg, reviewImgFileList.get(i));
		}
		
		return review.getId();
	}
	
	public List<Review> getReviewsByItemId(Long itemId){
		return reviewRepository.findByItemId(itemId);
	}
	
	public void updateReview(ReviewDto reviewDto) {
		
		Long Id = reviewDto.getId();
		
		Review review = reviewRepository.findById(Id)
							.orElseThrow(EntityNotFoundException::new);
		
		review.updateReview(reviewDto);
		
	}
	
	public void deleteReview(Long reviewId) throws Exception {
		
		Review review = reviewRepository.findById(reviewId)
								.orElseThrow(EntityNotFoundException::new);
		
		reviewRepository.delete(review);
		
		
		List<ReviewImg> reviewImg = review.getReviewImgList();
		
		String imgName= null;
		
		for(ReviewImg img : reviewImg) {
			
			imgName = img.getImgName();
			
			fileService.deleteFile(reviewImgLocation+"/"+imgName);
		}
		
		
		
		
		
		
	}
	

	
}
