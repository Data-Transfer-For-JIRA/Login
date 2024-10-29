package com.login.service;

import com.login.dao.RoleRepository;
import com.login.dao.UserRepository;
import com.login.dto.CreateAccountForm;
import com.login.entity.Role;
import com.login.entity.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User getUserInfo(String userId) {
        logger.info(":: LoginServiceImpl :: 사용자 정보 조회");
        User userInfo = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid User ID: " + userId));
        logger.info(":: LoginServiceImpl :: 사용자 정보 조회"+userId);
        return userInfo;
    }

    @Override
    public Role getRoleInfo(Long roleId) throws Exception {
        try {
            logger.info(":: LoginServiceImpl :: 권한 정보 조회");
            Role roleInfo = roleRepository.findById(roleId)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid Role ID: " + roleId));
            logger.info(":: LoginServiceImpl :: 권한 정보 조회"+roleInfo);
            return roleInfo;
        }catch (Exception e){
            logger.error("권한 조회 오류 발생 "+ e.getMessage());
            throw new Exception();
        }
    }

    @Override
    public List<Role> getAllRoleInfo() throws Exception {
        try {
            logger.info(":: LoginServiceImpl :: 권한 정보 조회");

            List<Role> returnData = roleRepository.findAll();

            return returnData;
        }catch (Exception e){
            logger.error("권한 조회 오류 발생 "+ e.getMessage());
            throw new Exception();
        }
    }

    @Override
    public User createAccount(CreateAccountForm createAccountForm) {
        String userId = createAccountForm.getUserId();
        String userName = createAccountForm.getUserName();
        String userPwd = createAccountForm.getUserPwd();
        Set<Long> userRoles = createAccountForm.getRole();


        // 역할 ID로 Role 객체 조회
        Set<Role> roles = userRoles.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid role ID: " + roleId)))
                .collect(Collectors.toSet());

        // User 생성 및 저장
        User user = User.builder()
                .userId(userId)
                .username(userName)
                .password(passwordEncoder.encode(userPwd))
                .roles(roles)
                .build();

        // User 저장
        User returnData = userRepository.save(user);

        return returnData;
    }
}
