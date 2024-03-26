package com.chat.project.chat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityContextController {

    @GetMapping("/current-user-info")
    public String getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Authentication 객체에서 사용자의 정보를 가져옵니다.
            String currentUserName = authentication.getName();
            // 또한, 다른 정보도 확인할 수 있습니다. 예를 들어, 권한 목록:
            String authorities = authentication.getAuthorities().toString();

            // 로그로 현재 인증된 사용자의 이름과 권한을 기록합니다.
            System.out.println("Current User: " + currentUserName);
            System.out.println("Authorities: " + authorities);

            return "Current User: " + currentUserName + ", Authorities: " + authorities;
        }

        return "No authentication information available";
    }
}
