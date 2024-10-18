package com.demo.springboot.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(timeToLive = 15)
public class CachedUserEntityByEmail {

    @Id
    private String email;
    private UserEntity user;

    public CachedUserEntityByEmail(final UserEntity user) {
        email = user.getEmail();
        this.user = user;
    }
}
