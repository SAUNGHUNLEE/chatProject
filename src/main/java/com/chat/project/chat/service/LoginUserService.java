package com.chat.project.chat.service;


import com.chat.project.chat.dto.UserDTO;
import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import util.SmsCertificationDao;
import util.SmsCertificationUtil;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class LoginUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${file.path.member.profile}")
    private String memberPath;

    public LoginUserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }


    public UserDTO.ViewName getUserName(UserDTO userDTO){
        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("조건에 해당하는 사용자가 없습니다."));

        UserDTO.ViewName uv = UserDTO.ViewName.builder()
                .id(user.getId())
                .name(user.getName())
                .build();

        return uv;
    }

}
