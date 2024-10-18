package com.demo.springboot.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserRegistrationDto {

    @NotNull
    @NotBlank
    @Length(min = 1, max = 64)
    private String firstName;

    @Length(min = 1, max = 64)
    private String lastName;

    @NotNull
    @NotBlank
    @Email
    @Length(min = 1, max = 512)
    private String email;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 64)
    private String password;

    @NotNull
    @NotBlank
    @Length(min = 1, max = 64)
    private String confirmPassword;
}
