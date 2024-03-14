
package com.chat.project.chat.service.email;


import com.chat.project.chat.model.EmailToken;
import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
    private final EmailTokenService emailTokenService;
    private final UserRepository userRepository;

    @Transactional
    public boolean verifyEmail(String token) throws Exception{
        EmailToken findEmailToken = emailTokenService.findByIdAndExpirationDateAfterAndExpired(token);
        // 성공 시 유저의 인증내용 변경
        Optional<User> findMember = userRepository.findByEmail(findEmailToken.getEmail());
        findEmailToken.setTokenToUsed(); // 사용 완료
        if(findMember.isPresent()){
            User userEntity = findMember.get();
            userEntity.setState(1); // 이메일 인증
            userRepository.save(userEntity); // 엔티티 수정
            return true;
        }else{
            throw new RuntimeException("토큰 에러");
        }
    }

}

