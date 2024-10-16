package com.demo.springboot.user;

public interface UserService {

    UserDto createUser(final UserDto user);

    UserDto updateUser(final UserDto user);
}
