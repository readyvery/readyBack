package com.readyvery.readyverydemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.readyvery.readyverydemo.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
