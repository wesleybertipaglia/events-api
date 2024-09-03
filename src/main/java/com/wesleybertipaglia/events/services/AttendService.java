package com.wesleybertipaglia.events.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.wesleybertipaglia.events.entities.Attendee;
import com.wesleybertipaglia.events.entities.Event;
import com.wesleybertipaglia.events.entities.User;
import com.wesleybertipaglia.events.mappers.EventMapper;
import com.wesleybertipaglia.events.mappers.UserMapper;
import com.wesleybertipaglia.events.records.event.EventResponseRecord;
import com.wesleybertipaglia.events.records.user.UserResponseRecord;
import com.wesleybertipaglia.events.repositories.AttendeeRepository;
import com.wesleybertipaglia.events.repositories.EventRepository;
import com.wesleybertipaglia.events.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AttendService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendeeRepository attendeeRepository;

    @Transactional
    public void attendEvent(UUID id, String tokenSubject) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));

        User user = userRepository.findById(UUID.fromString(tokenSubject))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (event.getOwner().getId().equals(user.getId())) {
            throw new IllegalStateException("Owner cannot attend their own event");
        } else if (attendeeRepository.existsByEventIdAndUserId(id, user.getId())) {
            throw new IllegalStateException("User is already attending this event");
        }

        Attendee attendee = new Attendee(event, user);
        attendeeRepository.save(attendee);
    }

    @Transactional
    public void unattendEvent(UUID id, String tokenSubject) {
        Attendee attendee = attendeeRepository.findByEventIdAndUserId(id,
                UUID.fromString(tokenSubject))
                .orElseThrow(() -> new EntityNotFoundException("User is not attending this event"));

        attendeeRepository.delete(attendee);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseRecord> listAttendees(UUID id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Event not found"));

        return attendeeRepository.findAllByEventId(id, pageable)
                .map(Attendee::getUser)
                .map(UserMapper::entityToResponseRecord);
    }

    @Transactional(readOnly = true)
    public Page<EventResponseRecord> listAttendancesByUserId(int page, int size, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);

        return attendeeRepository.findAllByUserId(user.getId(), pageable)
                .map(attendee -> EventMapper.entityToResponseRecord(attendee.getEvent()));
    }

    @Transactional(readOnly = true)
    public boolean isAttending(UUID eventId, String tokenSubject) {
        return attendeeRepository.existsByEventIdAndUserId(eventId, UUID.fromString(tokenSubject));
    }

}
