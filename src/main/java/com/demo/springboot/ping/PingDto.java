package com.demo.springboot.ping;

import com.demo.springboot.core.text.JsonSerializer;
import com.demo.springboot.core.utilities.DateTimeFormatter;
import lombok.*;

import java.util.Date;

@Getter
@Builder
// @Setter
// @NoArgsConstructor(access = AccessLevel.PRIVATE)
// @AllArgsConstructor
public class PingDto {
    private String version;
    private String timestamp = DateTimeFormatter.formatDate(new Date());
    private int statusCode;
    private String message;

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}
