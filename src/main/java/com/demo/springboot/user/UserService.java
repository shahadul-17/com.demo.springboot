package com.demo.springboot.user;

public interface UserService {

    UserEntity findByUserId(final Long userId);
    UserEntity findByPublicId(final String publicId);
    UserEntity findByEmail(final String email);

    UserDto signup(final UserRegistrationDto registration);

    UserDto login(final UserLoginDto loginDto);

    UserDto createUser(final UserDto user);

    UserDto updateUser(final UserDto user);
}
