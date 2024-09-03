package com.wesleybertipaglia.events.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wesleybertipaglia.events.records.event.EventResponseRecord;
import com.wesleybertipaglia.events.records.user.UserResponseRecord;
import com.wesleybertipaglia.events.services.EventService;
import com.wesleybertipaglia.events.services.UserService;
import com.wesleybertipaglia.events.services.AttendService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private AttendService attendService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<UserResponseRecord>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.listUsers(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserResponseRecord> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<Page<EventResponseRecord>> listEventsByUserId(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.listEventsByUserId(page, size, id));
    }

    @GetMapping("/{id}/attendances")
    public ResponseEntity<Page<EventResponseRecord>> listAttendancesByUserId(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(attendService.listAttendancesByUserId(page, size, id));
    }

}