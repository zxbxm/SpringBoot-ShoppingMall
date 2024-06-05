package com.tjoeun.shop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/member/login")
		                .defaultSuccessUrl("/", true)
		                .usernameParameter("email")
				            .failureUrl("/member/login/error").and().logout()
				            .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
				            .logoutSuccessUrl("/");

		http.authorizeRequests().requestMatchers("/css/**", "/js/**", "/image/**").permitAll()
                    				.requestMatchers("/", "/member/**", "/item/**", "/images/**","review/**").permitAll()
                    				.requestMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated();

		// 관리자로 로그인 요청하기
		http.exceptionHandling(exception -> exception.accessDeniedHandler(new CustomAccessDeniedHandler()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}