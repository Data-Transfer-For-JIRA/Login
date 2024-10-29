package com.login.controller;

import com.login.dto.CreateAccountForm;
import com.login.entity.Role;
import com.login.entity.User;
import com.login.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 사용자 정보 조회
    @ResponseBody
    @RequestMapping(
            value = {"/userInfo"},
            method = {RequestMethod.GET}
    )
    public User getUserInfo(@RequestParam String userId){
        logger.info(":: LoginController :: 사용자 정보 조회");
        return loginService.getUserInfo(userId);
    }

    @ResponseBody
    @RequestMapping(
            value = {"/roleInfo"},
            method = {RequestMethod.GET}
    )
    public Role getRoleInfo(@RequestParam Long roleId) throws Exception{
        logger.info(":: LoginController :: 사용자 권한 정보 조회");
        return loginService.getRoleInfo(roleId);
    }
    @ResponseBody
    @RequestMapping(
            value = {"/roleAllInfo"},
            method = {RequestMethod.GET}
    )
    public List<Role> getAllRoleInfo() throws Exception{
        logger.info(":: LoginController :: 사용자 권한 정보 조회");
        return loginService.getAllRoleInfo();
    }

    // 사용자 등록
    @ResponseBody
    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST}
    )
    public User createAccount(@RequestBody CreateAccountForm createAccountForm){
        logger.info(":: LoginController :: 사용자 계정 생성");
        return loginService.createAccount(createAccountForm);
    }


}
