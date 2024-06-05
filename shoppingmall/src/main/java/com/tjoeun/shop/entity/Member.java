package com.tjoeun.shop.entity;

import com.tjoeun.shop.constant.Role;
import com.tjoeun.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.*;

@Entity
@Getter @Setter @ToString
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;
    
    // cart 삭제
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL,
    		orphanRemoval = true)
    private Cart cart;
    
   	// 주문하는 상품들을 모아두는 List
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    
    // wish 삭제
	  @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval =
	  true) private Wish wish;
	  
    // review 삭제
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL,
    		orphanRemoval = true)
    private Review review;
    // member 삭제하기위해 만든것들

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, 
    		                              PasswordEncoder passwordEncoder){
    	
      // DTO --> Entity : ModelMapper 를 사용하지 않는 code
  		String password = passwordEncoder.encode(memberFormDto.getPassword());
  		
  		Member member = new Member();
  		member.setName(memberFormDto.getName());
  		member.setEmail(memberFormDto.getEmail());
  		member.setAddress(memberFormDto.getAddress());
  		member.setPassword(password);
  		member.setRole(Role.ADMIN);
  		
  		return member;
    }

}
