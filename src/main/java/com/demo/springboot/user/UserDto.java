package com.demo.springboot.user;

import com.demo.springboot.address.AddressDto;
import com.demo.springboot.core.text.JsonSerializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class UserDto {

    @NotNull
    private Long userId = 0L;

    @NotNull
    private Long entityVersion = 0L;

    @NotNull
    @Min(1)
    @Max(1)
    private String firstName;

    @Min(1)
    @Max(64)
    private String lastName;

    private String dateOfBirth;

    private Set<AddressDto> addresses;

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}
