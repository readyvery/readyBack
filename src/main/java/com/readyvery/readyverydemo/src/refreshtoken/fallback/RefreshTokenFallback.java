package com.readyvery.readyverydemo.src.refreshtoken.fallback;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refresh_token", indexes = {
    @Index(name = "idx_refresh_token", columnList = "refreshToken"),
    @Index(name = "idx_expires_at", columnList = "expiresAt")
})
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