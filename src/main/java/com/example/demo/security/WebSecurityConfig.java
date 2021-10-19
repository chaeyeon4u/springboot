package com.example.demo.security;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import lombok.extern.slf4j.Slf4j;

//추상 클래스 상속받음
//어노테이션 : 로그, 웹시큐리티
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	/*application.property에서 설정하면 datasource객체 만들어짐*/
	@Resource
	private DataSource dataSource;
	
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
		    .antMatchers("/security/manager/**")
		    .hasAuthority("ROLE_MANAGER")
		    .antMatchers("/security/user/**")
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
		//데이터베이스에서 가져올 사용자 정보 조회
	    //패스워드 인코딩 방법 설정
		auth.jdbcAuthentication()
		     .dataSource(dataSource)
		     .usersByUsernameQuery("SELECT mid, mpassword, menabled FROM member WHERE mid=?")
		     .authoritiesByUsernameQuery("SELECT mid, mrole FROM member WHERE mid=?")
		     .passwordEncoder(passwordEncoder);//default : DelegatingPasswordEncoder, password encoder 객체!
		     //.passwordEncoder(passwordEncoder());// 함수 호출해서 받아도 됨
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		log.info("WebSecurity 실행");
		//권한 계층 설정
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchyImpl());
		web.expressionHandler(handler);
		web.ignoring()//스프링 시큐리티를 거치지 않는 정적파일!
		   .antMatchers("/images/**")
		   .antMatchers("/bootstrap-4.6.0-dist/**")
		   .antMatchers("/css/**")
		   .antMatchers("/jquery/**")
		   .antMatchers("/favicon.ico");//아이콘
	}
	
	//주입받기
	@Resource
	private PasswordEncoder passwordEncoder;
	
	
	//관래객체 bean!
	//method가 리턴하는 xxx를 관리객체로 만들어주는 역할을 한다.
	//어디서든지 PasswordEncoder 주입받을 수 있다.
	@Bean
	public PasswordEncoder passwordEncoder() {
		//인코더 1.
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		
		//인코더 2 (bycript)
		//PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		return passwordEncoder;
	}
	
	//관리 객체로 만들어야한다!
	//권한 계층을 참조하기 위해 HttpSecurity에서 사용하기 때문에 관리빈으로 반드시 등록해야함
	@Bean
	public RoleHierarchyImpl roleHierarchyImpl() {
		RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
		roleHierarchyImpl.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
		
		return roleHierarchyImpl;
	}
}
