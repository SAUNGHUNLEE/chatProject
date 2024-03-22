
package com.chat.project.chat.service.email;


import com.chat.project.chat.model.EmailToken;
import com.chat.project.chat.model.User;
import com.chat.project.chat.persistence.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
    private final EmailTokenService emailTokenService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate; // RedisTemplate 추가


    public void processEmailVerificationSuccess(String userEmail, String token) {
        // 이메일 인증이 성공하면, Redis에 사용자의 이메일을 저장
        // 여기서 token은 이메일 인증에 사용된 토큰이며, 이를 키로 사용
        redisTemplate.opsForValue().set(token, userEmail, 1, TimeUnit.MINUTES); // 1분 동안 유효
    }
    @Transactional
    public boolean verifyEmail(String token) throws Exception{
        EmailToken findEmailToken = emailTokenService.findByIdAndExpirationDateAfterAndExpired(token);

       /*
        Optional<User> findMember = userRepository.findByEmail(findEmailToken.getEmail());
        if(findMember.isPresent()){
            User userEntity = findMember.get();
            userEntity.setState(1); // 이메일 인증
            userRepository.save(userEntity); // 엔티티 수정
            return true;
        }else{
            throw new RuntimeException("토큰 에러");
        }*/
        // 이메일 인증이 성공하면 Redis에 저장
        processEmailVerificationSuccess(findEmailToken.getEmail(), token);
        System.out.println("Redis에 저장됨: Token = " + token + ", Email = " + findEmailToken.getEmail());
        //findEmailToken.setTokenToUsed(); // 사용 완료
        return true;
    }

}

