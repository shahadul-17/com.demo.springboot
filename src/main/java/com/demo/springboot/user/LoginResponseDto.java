package com.demo.springboot.user;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String tokenType = "Bearer";
    private String accessToken;
    private UserDto user;
}
