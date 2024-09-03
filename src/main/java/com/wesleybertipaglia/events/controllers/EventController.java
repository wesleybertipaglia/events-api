package com.wesleybertipaglia.events.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wesleybertipaglia.events.records.event.EventRequestRecord;
import com.wesleybertipaglia.events.records.event.EventResponseRecord;
import com.wesleybertipaglia.events.records.user.UserResponseRecord;
import com.wesleybertipaglia.events.services.AttendService;
import com.wesleybertipaglia.events.services.EventService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private AttendService attendService;

    @PostMapping
    public ResponseEntity<EventResponseRecord> createEvent(@RequestBody EventRequestRecord eventRequest,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(eventService.createEvent(eventRequest, token.getName()));
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseRecord>> listEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "") String query) {
        return ResponseEntity.ok(eventService.listEvents(page, size, sort, direction, query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseRecord> getEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseRecord> updateEvent(@PathVariable UUID id,
            @RequestBody EventRequestRecord eventRequest,
            JwtAuthenticationToken token) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventRequest, token.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id, JwtAuthenticationToken token) {
        eventService.deleteEvent(id, token.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/attend")
    public ResponseEntity<Void> attendEvent(@PathVariable UUID id, JwtAuthenticationToken token) {
        attendService.attendEvent(id, token.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/unattend")
    public ResponseEntity<Void> unattendEvent(@PathVariable UUID id, JwtAuthenticationToken token) {
        attendService.unattendEvent(id, token.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/attendees")
    public ResponseEntity<Page<UserResponseRecord>> listAttendees(@PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(attendService.listAttendees(id, page, size));
    }

    @GetMapping("{id}/is-attending")
    public ResponseEntity<Boolean> isAttending(@PathVariable UUID id, JwtAuthenticationToken token) {
        return ResponseEntity.ok(attendService.isAttending(id, token.getName()));
    }

    @GetMapping("{id}/is-owner")
    public ResponseEntity<Boolean> isOwner(@PathVariable UUID id, JwtAuthenticationToken token) {
        return ResponseEntity.ok(eventService.isOwner(id, token.getName()));
    }

}