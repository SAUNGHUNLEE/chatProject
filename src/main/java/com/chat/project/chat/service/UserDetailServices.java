package com.chat.project.chat.service;


import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServices implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public User loadUserByUsername(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }

}