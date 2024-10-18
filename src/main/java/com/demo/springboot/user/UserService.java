package com.demo.springboot.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    UserEntity findByUserId(final Long userId);
    UserEntity findByPublicId(final String publicId);
    UserEntity findByEmail(final String email);
    Optional<UserEntity> findByCreationDateEx(final Date createdAt);

    UserDto signup(final UserRegistrationDto registration);

    LoginResponseDto login(final UserLoginDto loginDto);

    UserDto createUser(final UserDto user);

    UserDto updateUser(final UserDto user);
}
