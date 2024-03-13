package com.chat.project.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
