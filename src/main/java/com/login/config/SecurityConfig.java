package com.login.config;

import com.login.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Spring이 이 클래스를 빈으로 등록 및 설정을 적용
@EnableWebSecurity // Spring Security 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    // 애플리케이션의 HTTP 보안 설정 정의
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .authorizeRequests() // 요청에 대한 보안 설정 시작
                    .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .antMatchers( "/api/login/**").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN") // /admin/** 경로는 ADMIN 역할이 있어야 접근 가능
                    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") // /user/** 경로는 USER와 ADMIN 모두 접근 가능
                    .antMatchers("/open/**").permitAll()
                    .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                .and()
                .formLogin()  // 기본 로그인 폼 사용
                    .loginPage("/open/login.html") // 커스텀 로그인 페이지 경로 설정
                    .loginProcessingUrl("/login") // 폼 제출 처리 URL (기본 /login)
                    .defaultSuccessUrl("/test/index.html", true) // 로그인 성공 시 리다이렉트 경로 설정
                    .permitAll() // 모든 사용자에게 로그인 페이지 접근 허용
                .and()
                .logout() // 로그아웃 설정
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/open/login.html")
                    .permitAll() // 모든 사용자에게 로그아웃 페이지 접근 허용
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/open/403.html");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 사용
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder()); // PasswordEncoder 설정
    }
}
