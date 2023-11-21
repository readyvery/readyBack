package com.readyvery.readyverydemo.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public class BaseTimeEntity {

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "last_modified_at")
	private LocalDateTime lastModifiedAt;
}
