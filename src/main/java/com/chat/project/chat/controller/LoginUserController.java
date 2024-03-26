package com.chat.project.chat.controller;

import com.chat.project.chat.dto.UserDTO;
import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.UserRepository;
import com.chat.project.chat.service.CustomerUserDetails;
import com.chat.project.chat.service.LoginUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Slf4j
@Controller
@RequestMapping("/login")
public class LoginUserController {
    private final LoginUserService loginUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public LoginUserController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @GetMapping("/main")
    public String main(Model model, @AuthenticationPrincipal CustomerUserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getName();
            model.addAttribute("name", username);
            System.out.println(username + "유저 정보");
        } else {
            System.out.println("인증된 사용자 정보가 없습니다.");
        }
        return "main";
    }



}