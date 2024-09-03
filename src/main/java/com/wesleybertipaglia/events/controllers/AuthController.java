package com.wesleybertipaglia.events.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.wesleybertipaglia.events.records.auth.SignInRequestRecord;
import com.wesleybertipaglia.events.records.auth.SignInResponseRecord;
import com.wesleybertipaglia.events.records.auth.SignUpRequestRecord;
import com.wesleybertipaglia.events.records.user.UserResponseRecord;
import com.wesleybertipaglia.events.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public SignInResponseRecord signin(@RequestBody SignInRequestRecord signInRequest) {
        return authService.signIn(signInRequest);
    }

    @PostMapping("/signup")
    public UserResponseRecord signup(@RequestBody SignUpRequestRecord signUpRequest) {
        return authService.signUp(signUpRequest);
    }

}