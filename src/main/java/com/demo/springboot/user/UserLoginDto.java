package com.demo.springboot.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginDto {

    @NotNull
    @NotBlank
    @Email
    @Length(min = 1, max = 512)
    private String email;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 64)
    private String password;
}
