package com.chat.project.chat.service;


import com.chat.project.chat.dto.UserDTO;
import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, MemberRepository memberRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.memberRepository = memberRepository;
    }

    public int save(UserDTO userDTO) {
        return memberRepository.save(User.builder()
                .email(userDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                .name(userDTO.getName())
                .joinDay(LocalDate.now())
                .state(0)
                .role(0)
                .birthDay(userDTO.getBirthDay())
                .build()).getId();
    }

}
