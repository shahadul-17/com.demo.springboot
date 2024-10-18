package com.demo.springboot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v{version}/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(
            @PathVariable final String version,
            @RequestBody final UserRegistrationDto registration
    ) {
        final var _user = userService.signup(registration);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(_user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> createUser(
            @PathVariable final String version,
            @RequestBody final UserLoginDto loginDto
    ) {
        final var _user = userService.login(loginDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(_user);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @PathVariable final String version,
            @RequestBody final UserDto user
    ) {
        final var _user = userService.createUser(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(_user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable final String version,
            @PathVariable final String userId,
            @RequestBody final UserDto user
    ) {
        // setting the user ID...
        user.setUserId(userId);

        final var _user = userService.updateUser(user);

        System.out.println(_user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(_user);
    }
}
