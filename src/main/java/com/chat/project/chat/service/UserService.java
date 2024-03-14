package com.chat.project.chat.service;


import com.chat.project.chat.dto.UserDTO;
import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    public UserDTO save(UserDTO userDTO) {

    /*    final String email = userDTO.getEmail();
        if(memberRepository.existsByEmail(email)){
            throw new RuntimeException("해당 email이 이미 존재합니다.");
        }
        // 닉네임 중복 체크
        final String name = userDTO.getName();
        if(memberRepository.existsByName(name)){
            throw new RuntimeException("해당 이름이 이미 존재합니다.");
        }*/

        try{
            //dto -> entity
            User user = User.builder()
                    .email(userDTO.getEmail())
                    .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                    .name(userDTO.getName())
                    .joinDay(LocalDate.now())
                    .state(0)
                    .role(userDTO.getRole())
                    .birthDay(userDTO.getBirthDay())
                    .build();
            userRepository.save(user).getId();

            //entity -> dto
            return user.toUserDTO();


        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("UnauthMemberService.add() : 에러 발생.");
        }

    }

}
