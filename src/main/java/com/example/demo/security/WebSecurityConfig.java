package com.example.demo.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

//추상 클래스 상속받음
//어노테이션 : 로그, 웹시큐리티
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("HttpSecurity 실행");
		//로그인 방식 설정
		//form으로 록인 진행할거고, 로그인 페이지 경로는 "security/loginForm" 야
		http.formLogin()
		    .loginPage("/security/loginForm")//DEFAULT : /login(Get)
		    .usernameParameter("mid")//loginForm의 username(아이디) DEFAULT : username
		    .passwordParameter("mpassword")//loginForm의 password DEFAULT : password
		    .loginProcessingUrl("/login")//default: /login, 반드시 POST방식
		    .defaultSuccessUrl("/security/content")//login 성공시 경로
		    .failureUrl("/security/loginError");//login 실패시 경로 DEFAULT : login?error
		
		//로그아웃 설정
		http.logout()
		    .logoutUrl("/logout")//DEFAULT : /logout
		    .logoutSuccessUrl("/security/content");
		
		//url 권한 설정
		http.authorizeRequests()//권한 요청
		    .antMatchers("/security/admin/**")//**는 앞에 /security/admin만 맞으면 통과!
		    .hasAuthority("ROLE_ADMIN")
		    .antMatchers("/security/MANAGER/**")
		    .hasAuthority("ROLE_MANAGER")
		    .antMatchers("/security/USER/**")
		    .hasAuthority("ROLE_USER")
		    .antMatchers("/**").permitAll();
		//권한 없음(403)일 경우
		http.exceptionHandling().accessDeniedPage("/security/accessDenied");
		//CSRF 비활성화
		http.csrf().disable();
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("AuthenticationManagerBuilder 실행");
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		log.info("WebSecurity 실행");
	}
}
