package com.chat.project.chat.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PhoneVerifyDTO {

    @Getter
    public static class SmsCertificationRequest {

        private String phone;
        private String verification; //인증 번호

    }



}
