package com.tjoeun.shop.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tjoeun.shop.entity.Member;
import com.tjoeun.shop.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Member findMember(String email) {
    	return memberRepository.findByEmail(email);
    }
    
    public void updatePassword(String email, String currentPassword, String newPassword) {
      // 로그인된 사용자가 맞다면 회원 정보를 확실히 가져올 수 있다고 가정
      Member member = memberRepository.findByEmail(email);

      // 현재 비밀번호가 맞는지 확인
      if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
          throw new RuntimeException("비밀번호가 틀립니다");
      }
      
      if (currentPassword.equals(newPassword)) {
        throw new RuntimeException("현재 비밀번호와 새 비밀번호가 동일합니다.");
    }

      // 새로운 비밀번호로 업데이트
      member.setPassword(passwordEncoder.encode(newPassword));
      memberRepository.save(member);
  }


    

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}