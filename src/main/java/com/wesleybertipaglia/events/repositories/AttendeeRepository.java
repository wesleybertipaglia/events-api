package com.wesleybertipaglia.events.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wesleybertipaglia.events.entities.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, UUID> {
    Optional<Attendee> findByEventIdAndUserId(UUID eventId, UUID userId);

    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);

    Page<Attendee> findAllByEventId(UUID eventId, Pageable pageable);

    Page<Attendee> findAllByUserId(UUID userId, Pageable pageable);
}