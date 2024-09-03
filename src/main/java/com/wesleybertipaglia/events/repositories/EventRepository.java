package com.wesleybertipaglia.events.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wesleybertipaglia.events.entities.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> findByIdAndOwnerId(UUID id, UUID ownerId);

    Boolean existsByIdAndOwnerId(UUID id, UUID ownerId);

    Page<Event> findAllByOwnerId(UUID ownerId, Pageable pageable);

    Page<Event> findAllByTitleContainingIgnoreCase(String query, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Event> searchByTitleOrDescription(@Param("query") String query, Pageable pageable);
}