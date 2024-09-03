package com.wesleybertipaglia.events.records.user;

import java.util.UUID;

import org.springframework.hateoas.Links;

public record UserResponseRecord(UUID id, String name, String email, String image, Links links) {
}