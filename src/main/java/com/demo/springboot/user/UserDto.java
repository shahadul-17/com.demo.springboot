package com.demo.springboot.user;

import com.demo.springboot.address.AddressDto;
import com.demo.springboot.core.text.JsonSerializer;
import com.demo.springboot.core.utilities.StringUtilities;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserDto {

    @NotNull
    private String userId = StringUtilities.getEmptyString();

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

    private Date createdAt;

    private Date updatedAt;

    private Set<AddressDto> addresses;

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}
