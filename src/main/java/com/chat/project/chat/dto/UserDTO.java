package com.chat.project.chat.dto;

import com.chat.project.chat.model.User;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    //name joinday profile role state birthday
    private int id;
    private String email;
    private String password;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDay;
    private int role;
    private int state;
    private LocalDate birthDay;
    private String profile;
    private int phone;

    private List<MultipartFile> profileImgRequest;

    public boolean checkProfileImgRequestNull() {
        return this.profileImgRequest != null;
    }


    @Data
    @Builder
    @AllArgsConstructor
    public static class ViewName{
        private String name;
        private int id;

    }

    @Getter
    @NoArgsConstructor
    public static class ViewConfirmEmail{
        private String email;

    }

    @Getter
    public static class SmsCertificationRequest {
        private String phone;
        private String verification; //인증 번호
    }

    //이메일 찾기(핸드폰+이름으로)
    @Getter
    public static class LookForEmail{
        private String name;
        private int phone;
    }

    //이메일 검증(핸드폰+이름)
    @Getter
    public static class CheckForEmail{
        private String name;
        private String email;
        private int phone;

    }

    //비밀번호 변경
    @Getter
    @Setter
    public static class ChangePassWord{
        private String email;
        private String token;
        private String curpw;
        private String chpw;
    }


}
