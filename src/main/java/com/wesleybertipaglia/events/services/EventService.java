package com.wesleybertipaglia.events.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.wesleybertipaglia.events.entities.Event;
import com.wesleybertipaglia.events.entities.User;
import com.wesleybertipaglia.events.mappers.EventMapper;
import com.wesleybertipaglia.events.records.event.EventRequestRecord;
import com.wesleybertipaglia.events.records.event.EventResponseRecord;
import com.wesleybertipaglia.events.repositories.EventRepository;
import com.wesleybertipaglia.events.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public EventResponseRecord createEvent(EventRequestRecord eventRequest, String tokenSubject) {
        User user = userRepository.findById(UUID.fromString(tokenSubject))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Event event = EventMapper.createRequestToEntity(eventRequest, user);
        eventRepository.save(event);
        return EventMapper.entityToResponseRecord(event);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseRecord> listEvents(int page, int size, String sort, String direction, String query) {
        Sort sortBy = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortBy);

        if (!query.isEmpty()) {
            return eventRepository.searchByTitleOrDescription(query, pageable)
                    .map(EventMapper::entityToResponseRecord);
        }

        return eventRepository.findAll(pageable).map(EventMapper::entityToResponseRecord);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseRecord> listEventsByUserId(int page, int size, UUID userId) {
        Pageable pageable = PageRequest.of(page, size);

        return eventRepository.findAllByOwnerId(userId, pageable)
                .map(EventMapper::entityToResponseRecord);
    }

    @Transactional(readOnly = true)
    public EventResponseRecord getEvent(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return EventMapper.entityToResponseRecord(event);
    }

    @Transactional
    public EventResponseRecord updateEvent(UUID id, EventRequestRecord eventRequest, String tokenSubject) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));

        if (!isOwner(id, tokenSubject)) {
            throw new AccessDeniedException("User dont have permission to update this event");
        }

        if (eventRequest.title() != null) {
            event.setTitle(eventRequest.title());
        }
        if (eventRequest.description() != null) {
            event.setDescription(eventRequest.description());
        }
        if (eventRequest.location() != null) {
            event.setLocation(eventRequest.location());
        }
        if (eventRequest.date() != null) {
            event.setDate(eventRequest.date());
        }
        if (eventRequest.image() != null) {
            event.setImage(eventRequest.image());
        }

        eventRepository.save(event);
        return EventMapper.entityToResponseRecord(event);
    }

    @Transactional
    public void deleteEvent(UUID id, String tokenSubject) {
        Event event = eventRepository.findByIdAndOwnerId(id, UUID.fromString(tokenSubject))
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        eventRepository.delete(event);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(UUID eventId, String tokenSubject) {
        return eventRepository.existsByIdAndOwnerId(eventId, UUID.fromString(tokenSubject));
    }

}
