package com.readyvery.readyverydemo.security.jwt.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.readyvery.readyverydemo.domain.Role;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomUserDetails implements UserDetails {

	private Long id;
	private String email;
	private String password;
	private String accessToken;
	private Role role;
	private Collection<? extends GrantedAuthority> authorities;

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true; // 혹은 실제 계정 상태에 따라 변경
	}

	@Override
	public boolean isAccountNonLocked() {
		return true; // 혹은 실제 계정 상태에 따라 변경
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 혹은 실제 자격 증명 상태에 따라 변경
	}

	@Override
	public boolean isEnabled() {
		return true; // 혹은 실제 계정 활성화 상태에 따라 변경
	}
}
