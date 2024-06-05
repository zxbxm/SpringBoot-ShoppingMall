package com.tjoeun.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tjoeun.shop.entity.ReviewImg;
import com.tjoeun.shop.repository.ReviewImgRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewImgService {

	@Value("${reviewImgLocation}")
	private String reviewImgLocation;
	
	private final ReviewImgRepository reviewImgRepository;
	
	private final FileService fileService;
	
	public void saveReviewImg(ReviewImg reviewImg, MultipartFile reviewImgFile)throws Exception {
		
		String oriImgName = reviewImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		log.info("Original Image Name: {}", oriImgName);
    log.info("Review Image File: {}", reviewImgFile);
		
		if(StringUtils.hasText(oriImgName)) {
			imgName = fileService.uploadFile(reviewImgLocation, oriImgName, 
																		reviewImgFile.getBytes());
			imgUrl = "/images/reviewImages/" + imgName;
		}
		log.info(">>>>>>>>>>>>>>>> reviewImgFile2" + reviewImgFile);
		reviewImg.updateReviewImg(oriImgName, imgName, imgUrl);
		reviewImgRepository.save(reviewImg);
	}
	
	
	public void updateItemImg(Long reviewImgId, MultipartFile reviewImgFile) throws Exception {
		if(!reviewImgFile.isEmpty()) {
			
			ReviewImg savedReviewImg = reviewImgRepository.findById(reviewImgId)
										.orElseThrow(EntityNotFoundException::new);
			
			if(StringUtils.hasText(savedReviewImg.getImgName())) {
				fileService.deleteFile(reviewImgLocation+"/"+
												savedReviewImg.getImgName());
			}
			
			String oriImgName = reviewImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(reviewImgLocation, oriImgName, reviewImgFile.getBytes());
			String imgUrl = "/images/reviewImages/" +imgName;
			savedReviewImg.updateReviewImg(oriImgName, imgName, imgUrl);
			
			System.out.println("ImgUrl : " + imgUrl);
			
			
		}
		
		
		
	}
	
	
	
	
	
	
}
