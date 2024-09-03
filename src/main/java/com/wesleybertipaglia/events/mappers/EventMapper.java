package com.wesleybertipaglia.events.mappers;

import com.wesleybertipaglia.events.entities.Event;
import com.wesleybertipaglia.events.entities.User;
import com.wesleybertipaglia.events.records.event.EventRequestRecord;
import com.wesleybertipaglia.events.records.event.EventResponseRecord;
import com.wesleybertipaglia.events.controllers.EventController;
import com.wesleybertipaglia.events.controllers.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class EventMapper {
    public static EventResponseRecord entityToResponseRecord(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        } else if (event.getId() == null) {
            throw new IllegalArgumentException("Event id cannot be null");
        } else if (event.getTitle() == null) {
            throw new IllegalArgumentException("Event title cannot be null");
        } else if (event.getDescription() == null) {
            throw new IllegalArgumentException("Event description cannot be null");
        } else if (event.getLocation() == null) {
            throw new IllegalArgumentException("Event location cannot be null");
        } else if (event.getDate() == null) {
            throw new IllegalArgumentException("Event date cannot be null");
        }

        event.add(linkTo(methodOn(EventController.class).getEvent(event.getId())).withSelfRel());
        event.add(linkTo(methodOn(UserController.class).getUser(event.getOwner().getId())).withRel("owner"));
        event.add(linkTo(methodOn(EventController.class).listAttendees(event.getId(), 0, 10)).withRel("attendees"));

        return new EventResponseRecord(event.getId(), event.getTitle(), event.getDescription(), event.getLocation(),
                event.getDate(), event.getImage(), event.getLinks());
    }

    public static Event createRequestToEntity(EventRequestRecord eventRequest, User owner) {
        if (eventRequest == null) {
            throw new IllegalArgumentException("Event request cannot be null");
        } else if (eventRequest.title() == null) {
            throw new IllegalArgumentException("Event title cannot be null");
        } else if (eventRequest.description() == null) {
            throw new IllegalArgumentException("Event description cannot be null");
        } else if (eventRequest.location() == null) {
            throw new IllegalArgumentException("Event location cannot be null");
        } else if (eventRequest.date() == null) {
            throw new IllegalArgumentException("Event date cannot be null");
        }

        return new Event(eventRequest.title(), eventRequest.description(), eventRequest.location(),
                eventRequest.date(), eventRequest.image(), owner);
    }
}
