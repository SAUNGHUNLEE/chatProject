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

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired(){
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked(){
        return true; // true -> 잠금되지 않음
    }

    // 패스워드 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired(){
        return true; // true -> 만료되지 않음
    }

    // 계정 사용 가능 여부 변환
    @Override
    public boolean isEnabled(){
        return true; // true -> 사용 가능
    }

    public UserDTO toUserDTO(){
        return UserDTO.builder()
                .email(this.getEmail())
                .password(this.getPassword())
                .name(this.getName())
                .joinDay(LocalDate.now())
                .role(this.getRole())
                .state(this.getState())
                .birthDay(this.getBirthDay())
                .build();
    }


}
