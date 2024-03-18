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


    @Getter
    @NoArgsConstructor
    public static class ViewConfirmEmail{
        private String email;

    }



}
