package com.chat.project.chat.model;

import com.chat.project.chat.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L; // serialVersionUID 추가


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "join_day")
    private LocalDate joinDay;

    @Column(name = "profile")
    private String profile;

    @Column(name = "role")
    private int role;  //0:학생 1:관리자

    @Column(name = "state")
    private int state; //0:미인증 1:인증

    @Column(name = "birthday")
    private LocalDate birthDay;

    @Column(name = "phone")
    private int phone;

    public UserDTO toUserDTO(){
        return UserDTO.builder()
                .email(this.getEmail())
                .password(this.getPassword())
                .name(this.getName())
                .joinDay(LocalDate.now())
                .role(this.getRole())
                .state(this.getState())
                .birthDay(this.getBirthDay())
                .profile(this.getProfile())
                .build();
    }


}
