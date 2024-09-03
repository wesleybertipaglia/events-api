package com.wesleybertipaglia.events.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import com.wesleybertipaglia.events.entities.User;
import com.wesleybertipaglia.events.mappers.UserMapper;
import com.wesleybertipaglia.events.records.user.UserRequestRecord;
import com.wesleybertipaglia.events.records.user.UserResponseRecord;
import com.wesleybertipaglia.events.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserResponseRecord> listUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(UserMapper::entityToResponseRecord);
    }

    @Transactional(readOnly = true)
    public UserResponseRecord getUser(UUID id) {
        return UserMapper.entityToResponseRecord(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    @Transactional(readOnly = true)
    public UserResponseRecord getCurrentUser(String tokenSubject) {
        return UserMapper.entityToResponseRecord(userRepository.findById(UUID.fromString(tokenSubject))
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    @Transactional
    public Optional<UserResponseRecord> updateCurrentUser(UserRequestRecord userRequest, String tokenSubject) {
        User user = userRepository.findById(UUID.fromString(tokenSubject))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userRequest.name() != null) {
            user.setName(userRequest.name());
        }
        if (userRequest.email() != null) {
            user.setEmail(userRequest.email());
        }
        if (userRequest.password() != null) {
            user.setPassword(userRequest.password());
        }
        if (userRequest.image() != null) {
            user.setImage(userRequest.image());
        }

        userRepository.save(user);
        return Optional.of(UserMapper.entityToResponseRecord(user));
    }

    @Transactional
    public void deleteCurrentUser(String tokenSubject) {
        User user = userRepository.findById(UUID.fromString(tokenSubject))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);
    }

}
