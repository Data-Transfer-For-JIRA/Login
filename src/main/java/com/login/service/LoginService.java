package com.login.service;

import com.login.dto.CreateAccountForm;
import com.login.entity.Role;
import com.login.entity.User;

import java.util.List;

public interface LoginService {

    //사용자 정보 조회
    User getUserInfo(String userId);

    // 권한 정보 조회
    Role getRoleInfo(Long roleId) throws Exception;

    List<Role>  getAllRoleInfo() throws Exception;

    // 사용자 정보 등록
    User createAccount(CreateAccountForm createAccountForm) throws Exception;

}
