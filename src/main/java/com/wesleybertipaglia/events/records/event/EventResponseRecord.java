package com.wesleybertipaglia.events.records.event;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.hateoas.Links;

public record EventResponseRecord(UUID id, String title, String description, String location,
        LocalDate date, String image, Links links) {
}