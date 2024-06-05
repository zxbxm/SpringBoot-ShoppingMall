package com.tjoeun.shop.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tjoeun.shop.dto.MemberFormDto;
import com.tjoeun.shop.entity.Member;
import com.tjoeun.shop.repository.MemberRepository;
import com.tjoeun.shop.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping("/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

    	  log.info(">>>>>>>>>>>>>>>> memberFormDto : " + memberFormDto);
    	
    	  if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

    		try {
					// DTO --> Entity
					// 화면에서 받아온 회원가입 정보를 Member Entity 에 저장함
          Member member = Member.createMember(memberFormDto, passwordEncoder);
          log.info(">>>>>>>>>>>>>>>> member : " + member);
          Member savedMember = memberService.saveMember(member);
          log.info(">>>>>>>>>>>>>>>> savedMember : " + savedMember);
        } catch (IllegalStateException e){
            model.addAttribute("error", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String loginMember(){
    	log.info(">>>>>>>>>>>>>>>> login");
        return "/member/memberLoginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }
    
  	@GetMapping("/logout")
  	public String signout(HttpServletRequest request, HttpServletResponse response) {
  		log.info(">>>>>>>>>>>>>>>> logout");
  		
  		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
  		// session 정보 삭제하기
  		if(authentication != null) {
  			new SecurityContextLogoutHandler().logout(request, response, authentication);
  		}
  		 SecurityContextHolder.clearContext();
  		 request.getSession().invalidate();
  		return "redirect:/";
  	}
    
  	@GetMapping("login/accessDenied")
    public String accessDenied(Model model) {
    		
    		model.addAttribute("loginErrorMsg", "관리자로 로그인해 주세요");
    		
    		return "member/memberLoginForm";
    	}
  	
 // my page 들어갈때 로그인한 정보를 principal로 들고 같이 들어감
   	@GetMapping("/mypage")
   	public String mypage(Member member, Model model, Principal principal) {
   		String memberId = principal.getName();
       
       model.addAttribute("memberId", memberId);
   		
   		return "member/mypage";
   	}
   	
   	@GetMapping("/memberDelete")
   	public String delete(Member memeber, Model model, Principal principal) {
   		String username = principal.getName(); // 현재 로그인한 사용자의 아이디(이름)
   	  Member member = memberRepository.findByEmail(username);
   		
   		model.addAttribute("checkpassword", member.getPassword());
   		System.out.println(member.getPassword()); 
   		// 여기까지는 비밀번호를 가지고 들어감 
   		
   		return "member/memberDelete";
   	}
   	
   	
   	
   	// 마이 페이지에서 delete 를 post로 받는곳
   	// 회원 탈퇴 처리
     @PostMapping("/memberDelete")
     public String deleteMember(@RequestParam("checkpassword") String checkpassword, Model model,HttpServletRequest request) {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String username = authentication.getName();
         // 현재 email로 쓰고있음
         
         Member member = memberRepository.findByEmail(username);
         
         System.out.println("member.getPassword() : " + member.getPassword());
         System.out.println("checkpassword : " + checkpassword);
         System.out.println("비교 : " + passwordEncoder.matches(checkpassword, member.getPassword()));
         
         if (passwordEncoder.matches(checkpassword, member.getPassword())) {
         	
             // 비밀번호가 일치하면 회원 삭제 처리
             memberRepository.delete(member); // 부트의 원래 기능
             request.getSession().invalidate();
             return "forward:/member/logout"; // 로그아웃 시킴
         } else {
         	
         	  model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
             return "/member/memberDelete";
         }
     }
     
     
     @PostMapping("/password")
     public String updatePassword(@RequestParam("memberId") String memberId,
                                  @RequestParam("currentPassword") String currentPassword,
                                  @RequestParam("newPassword") String newPassword,
                                  HttpServletRequest request,
                                  RedirectAttributes redirectAttributes) {
         try {
             memberService.updatePassword(memberId, currentPassword, newPassword);
             
             // 로그아웃 처리 (세션 무효화)
             request.getSession().invalidate();
             
             redirectAttributes.addFlashAttribute("successMessage", "비밀번호 변경 완료, 로그인을 다시 해주세요.");
             return "redirect:/member/login"; // 로그인 페이지로 리다이렉트
         } catch (RuntimeException e) {
             redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
             return "redirect:/member/mypage"; 
         } catch (Exception e) {
             redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred.");
             return "redirect:/member/mypage"; 
         }
     }

}