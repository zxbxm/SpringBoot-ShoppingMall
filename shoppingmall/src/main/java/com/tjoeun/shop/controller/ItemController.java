package com.tjoeun.shop.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tjoeun.shop.dto.ItemFormDto;
import com.tjoeun.shop.dto.ItemSearchDto;
import com.tjoeun.shop.dto.ReviewDto;
import com.tjoeun.shop.entity.Item;
import com.tjoeun.shop.service.ItemService;
import com.tjoeun.shop.service.ReviewService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ReviewService reviewService;

    @GetMapping("/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping("/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 꼭 올려주세요");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 오류가 발생함 !!!");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "없는 상품입니다");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping("/admin/item/modify")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫 번째 상품 이미지는 꼭 올려주세요");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 오류가 발생함 !!!");
            return "item/itemForm";
        }

        return "redirect:/";
    }
    
    @PostMapping("/admin/item/delete")
    public String ItemDelete(@Valid ItemFormDto itemFormDto, Model model) {
    	
    	try {
    		itemService.deleteItem(itemFormDto);
    	}catch(Exception e) {
    		model.addAttribute("errorMessage","상품 삭제 중 오류가 발생함");
    	}
    	
    	
    	return "redirect:/";
    }

    @GetMapping({"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    }

    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId,Principal principal){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        
        
        
        
        log.info(">>>>>>>>>>>>>>>> itemId" + itemId);
        
        model.addAttribute("reviewDto", new ReviewDto());
        model.addAttribute("item", itemFormDto);
        model.addAttribute("itemId", itemId);
        model.addAttribute("userEmail", principal.getName());
        model.addAttribute("reviewList", reviewService.getReviewsByItemId(itemId));
        
        
        return "item/itemDtl";
    }

}