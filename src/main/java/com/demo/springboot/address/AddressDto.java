package com.demo.springboot.address;

import com.demo.springboot.core.text.JsonSerializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @NotNull
    private Long addressId = 0L;

    @NotNull
    private Long entityVersion = 0L;

    private String city;

    private String country;

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}
