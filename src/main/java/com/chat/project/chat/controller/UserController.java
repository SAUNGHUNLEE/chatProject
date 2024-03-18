package com.chat.project.chat.controller;

import com.chat.project.chat.dto.PhoneVerifyDTO;
import com.chat.project.chat.dto.UserDTO;
import com.chat.project.chat.persistence.UserRepository;
import com.chat.project.chat.service.UserService;
import com.chat.project.chat.service.email.EmailService;
import com.chat.project.chat.service.email.EmailTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/unauth")
public class UserController {
    private final UserService userService;
    @Autowired
    private EmailTokenService emailTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public String signup(UserDTO userDTO) {
        userService.save(userDTO);
        return "redirect:/unauth/login";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        session.setAttribute("user","SrpingUser");
        return "로그인 되었습니다.";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    @GetMapping("/check")
    public String checkSession(HttpSession session){
        return session.getAttribute("user") + "님";
    }
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean isEmailTaken = userRepository.existsByEmail(email); // 이메일 중복 검사
        Map<String, Boolean> response = new HashMap<>();
        response.put("isEmailTaken", isEmailTaken);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-name")
    public ResponseEntity<Map<String, Boolean>> checkName(@RequestParam String name) {
        boolean isNameTaken = userRepository.existsByName(name);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isNameTaken", isNameTaken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody UserDTO.ViewConfirmEmail email) {
        try {
            emailTokenService.createEmailToken(email.getEmail());
            System.out.println(email.getEmail() + " 이메일");
            // HashMap을 사용하여 JSON 형태의 응답을 생성
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "인증 메일이 전송되었습니다.");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("Error sending verification email: ", e);
            HashMap<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "인증 메일 전송에 실패했습니다.");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }


    //인증번호 보냄
    @PostMapping("/send-phone")
    public ResponseEntity<?> sendSms(@RequestBody PhoneVerifyDTO.SmsCertificationRequest requestDto) {
        try {
            userService.sendSms(requestDto);
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "인증 번호가 전송되었습니다.");
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            HashMap<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "인증 번호 전송에 실패했습니다.");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    //인증번호 확인
    @PostMapping("/confirm-phone")
    public ResponseEntity<?> SmsVerification(@RequestBody PhoneVerifyDTO.SmsCertificationRequest requestDto) {
        try {
            userService.verifySms(requestDto);
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "인증 성공.");
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            HashMap<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "인증 실패");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }



}