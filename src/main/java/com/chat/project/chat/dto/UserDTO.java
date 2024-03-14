package com.chat.project.chat.dto;

import com.chat.project.chat.model.User;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

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


    @Getter
    @NoArgsConstructor
    public static class ViewConfirmEmail{
        private String email;

    }

}
