package com.tjoeun.shop.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ResetPassword {
	
	
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = "123456789";  // 새 비밀번호를 여기에 입력
        String hashedPassword = encoder.encode(newPassword);
        System.out.println("Hashed Password: " + hashedPassword);
    
        // 데이터 베이스에서
        // UPDATE users SET password = '여기에_새로운_해시_비밀번호_입력' WHERE email = 'user@example.com';
    }
}

