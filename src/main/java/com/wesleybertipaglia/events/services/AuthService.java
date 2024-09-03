package com.wesleybertipaglia.events.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wesleybertipaglia.events.entities.User;
import com.wesleybertipaglia.events.mappers.UserMapper;
import com.wesleybertipaglia.events.records.auth.SignInRequestRecord;
import com.wesleybertipaglia.events.records.auth.SignInResponseRecord;
import com.wesleybertipaglia.events.records.auth.SignUpRequestRecord;
import com.wesleybertipaglia.events.records.user.UserResponseRecord;
import com.wesleybertipaglia.events.repositories.UserRepository;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public SignInResponseRecord signIn(SignInRequestRecord signInRequest) {
        User user = userRepository.findByEmail(signInRequest.email()).orElseThrow(
                () -> new BadCredentialsException("Invalid e-mail or password."));

        if (!passwordEncoder.matches(signInRequest.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid e-mail or password.");
        }

        Instant now = Instant.now();
        Long expirationTime = Long.valueOf(24 * 60 * 60 * 1000);

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuer("blog")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationTime))
                .claim("role", "USER")
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        UserResponseRecord userResponseRecord = UserMapper.entityToResponseRecord(user);
        return new SignInResponseRecord(token, expirationTime, userResponseRecord);
    }

    @Transactional
    public UserResponseRecord signUp(SignUpRequestRecord signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.email())) {
            throw new BadCredentialsException("E-mail already in use.");
        }

        User user = new User(signUpRequest.name(), signUpRequest.email(),
                passwordEncoder.encode(signUpRequest.password()), null);

        return UserMapper.entityToResponseRecord(userRepository.save(user));
    }

}
