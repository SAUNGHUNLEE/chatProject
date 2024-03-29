package com.chat.project.chat.persistence;


import com.chat.project.chat.model.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface EmailTokenRepository extends JpaRepository<EmailToken, String> {
    // 만료되지 않았으며 현재보다 이후에 만료되는 토큰정보를 가져옴
    Optional<EmailToken> findByEmailTokenIdAndExpirationDateAfterAndExpired(String emailTokenId, LocalDateTime now, boolean expired);

    Optional<EmailToken> findByEmailTokenId(String emailTokenId);
}
