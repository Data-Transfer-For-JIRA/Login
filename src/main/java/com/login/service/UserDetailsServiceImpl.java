package com.login.service;

import com.login.dao.UserRepository;
import com.login.entity.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        logger.info(":: UserDetailsServiceImpl :: 로그인 시도 : "+userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid User ID: " + userId));

        // 권한이 오직 ROLE_USER일 경우에만 유효 기간 검증 수행
        boolean isOnlyUserRole = user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_USER"));

        if (isOnlyUserRole && user.getPeriod() > 0) {
            LocalDateTime expirationDate = user.getCreatedDate().plusDays(user.getPeriod());
            if (LocalDateTime.now().isAfter(expirationDate)) {
                // 계정 삭제 로직
                throw new UsernameNotFoundException("계정이 만료되었습니다: " + userId);
            }
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword()) // 비밀 번호 검증
                .authorities(user.getRoles().stream()// 권한 설정
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()))
                .build();
    }
}
