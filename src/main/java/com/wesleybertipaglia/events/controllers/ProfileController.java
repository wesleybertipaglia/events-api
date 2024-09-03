package com.wesleybertipaglia.events.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wesleybertipaglia.events.records.event.EventResponseRecord;
import com.wesleybertipaglia.events.records.user.UserRequestRecord;
import com.wesleybertipaglia.events.records.user.UserResponseRecord;
import com.wesleybertipaglia.events.services.AttendService;
import com.wesleybertipaglia.events.services.EventService;
import com.wesleybertipaglia.events.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private AttendService attendService;

    @GetMapping
    public ResponseEntity<UserResponseRecord> getProfile(JwtAuthenticationToken token) {
        return ResponseEntity.ok(userService.getCurrentUser(token.getName()));
    }

    @PutMapping
    public ResponseEntity<UserResponseRecord> updateProfile(@RequestBody UserRequestRecord userRequestDTO,
            JwtAuthenticationToken token) {
        return ResponseEntity.of(userService.updateCurrentUser(userRequestDTO, token.getName()));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(JwtAuthenticationToken token) {
        userService.deleteCurrentUser(token.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events")
    public ResponseEntity<Page<EventResponseRecord>> listEventsOfProfile(
            JwtAuthenticationToken token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.listEventsByUserId(page, size, getUserId(token)));
    }

    @GetMapping("/attendances")
    public ResponseEntity<Page<EventResponseRecord>> listAttendancesOfProfile(
            JwtAuthenticationToken token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(attendService.listAttendancesByUserId(page, size, getUserId(token)));
    }

    private UUID getUserId(JwtAuthenticationToken token) {
        return UUID.fromString(token.getName());
    }

}