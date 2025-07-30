package com.readyvery.readyverydemo.src.refreshtoken.fallback;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenFallback {
    @Id
    private String userId;
    private String refreshToken;
    private LocalDateTime expiresAt;
} 