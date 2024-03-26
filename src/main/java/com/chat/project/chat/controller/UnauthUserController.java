package com.chat.project.chat.controller;

import com.chat.project.chat.dto.UserDTO;
import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.UserRepository;
import com.chat.project.chat.service.CustomerUserDetailServices;
import com.chat.project.chat.service.CustomerUserDetails;
import com.chat.project.chat.service.UnauthUserService;
import com.chat.project.chat.service.email.EmailTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/unauth")
public class UnauthUserController {
    private final UnauthUserService unauthUserService;
    @Autowired
    private EmailTokenService emailTokenService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public UnauthUserController(UnauthUserService unauthUserService) {
        this.unauthUserService = unauthUserService;
    }

    @PostMapping("/user")
    public String signup(UserDTO userDTO) {
        unauthUserService.save(userDTO);
        return "redirect:/unauth/login";
    }


    //시큐리티 사용 시, 검증은 UserDetailsService에 loadUserByUsername에서 함
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println(authentication + "인증정보,로그인 성공");
            return ResponseEntity.ok().body("로그인 성공");
        } catch (AuthenticationException e) {
            // 인증 실패 시 처리 로직
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }
    }


    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model) {

        /* 에러와 예외를 모델에 담아 view resolve */
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "login";
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

/*    @GetMapping("/main")
    public String mainPage() {
        return "main"; // templates 폴더 내의 main.html을 참조
    }*/



/*
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // "login.html" 타임리프 템플릿을 찾아 렌더링
    }
*/

/*
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        }

        return "redirect:/login";
    }
*/

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
    public ResponseEntity<?> sendSms(@RequestBody UserDTO.SmsCertificationRequest requestDto) {
        try {
            unauthUserService.sendSms(requestDto);
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
    public ResponseEntity<?> SmsVerification(@RequestBody UserDTO.SmsCertificationRequest requestDto) {
        try {
            unauthUserService.verifySms(requestDto);
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "인증 성공.");
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            HashMap<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "인증 실패");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    @PostMapping("/find-email")
    public ResponseEntity<?> findUserEmail(@RequestBody UserDTO.LookForEmail lookForEmailDTO) {
        try {
            UserDTO userDTO = unauthUserService.getUserEmail(lookForEmailDTO);
            System.out.println("이메일 찾기 성공");
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/check-user-email")
    public ResponseEntity<?> getCheckUserEmail(@RequestBody UserDTO.CheckForEmail checkForEmailDTO) {
        try {
            UserDTO userDTO = unauthUserService.getCheckEmail(checkForEmailDTO);
            System.out.println("이메일 매칭 성공");
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/change-pw")
    public String changePassword(@RequestBody Map<String, String> payload) throws Exception{
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");
        boolean isChanged = unauthUserService.changePasswordWithToken(token, newPassword);
        if (isChanged) {
            System.out.println("비밀번호 변경 성공. 다시 로그인 해주세요");
            return "redirect:/unauth/login";
        } else {
            System.out.println("변경 실패 다시 해 주세요");
            return "redirect:/unauth/change-password-form";
        }
    }





}