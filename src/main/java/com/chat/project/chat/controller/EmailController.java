package com.chat.project.chat.controller;

import com.chat.project.chat.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/unauth/user")
public class EmailController {

    private final EmailService emailService;
    @GetMapping("/confirm-email")
    public ResponseEntity<?> viewConfirmEmail(@RequestParam String token){
        try {
            boolean result = emailService.verifyEmail(token);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            // 주석
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
