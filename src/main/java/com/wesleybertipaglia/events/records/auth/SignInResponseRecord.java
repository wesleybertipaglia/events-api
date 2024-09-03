package com.wesleybertipaglia.events.records.auth;

import com.wesleybertipaglia.events.records.user.UserResponseRecord;

public record SignInResponseRecord(String accessToken, Long expiresIn, UserResponseRecord user) {
}