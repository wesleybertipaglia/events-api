package com.wesleybertipaglia.events.mappers;

import com.wesleybertipaglia.events.entities.User;
import com.wesleybertipaglia.events.records.user.UserResponseRecord;
import com.wesleybertipaglia.events.controllers.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserMapper {
    public static UserResponseRecord entityToResponseRecord(User user) {
        if (user == null) {
            return null;
        } else if (user.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null");
        } else if (user.getName() == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else if (user.getEmail() == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }

        user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).listEventsByUserId(user.getId(), 0, 10)).withRel("events"));
        user.add(linkTo(methodOn(UserController.class).listAttendancesByUserId(user.getId(), 0, 10))
                .withRel("attendances"));

        return new UserResponseRecord(user.getId(), user.getName(), user.getEmail(), user.getImage(),
                user.getLinks());
    }

}
