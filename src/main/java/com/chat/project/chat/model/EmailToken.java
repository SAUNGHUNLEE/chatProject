package com.chat.project.chat.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_token")
public class EmailToken {
    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L; // 이메일 토큰 만료 시간

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email_token_id")
    private String emailTokenId;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "expired")
    private boolean expired;

    @Column(name = "email")
    private String email;

    // 이메일 인증 토큰 생성
    public static EmailToken createEmailToken(String email) {
        EmailToken emailTokenEntity = new EmailToken();
        emailTokenEntity.emailTokenId = UUID.randomUUID().toString(); // UUID 생성
        emailTokenEntity.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE); // 5분 후 만료
        emailTokenEntity.expired = false;
        emailTokenEntity.email = email;

        return emailTokenEntity;
    }

    // 토큰 만료
    public void setTokenToUsed() {
        this.expired = true;
    }
}
