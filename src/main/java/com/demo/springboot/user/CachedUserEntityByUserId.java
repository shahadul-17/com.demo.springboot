package com.demo.springboot.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class CachedUserEntityByUserId {

    @Id
    private Long userId;
    private UserEntity user;

    public CachedUserEntityByUserId(final UserEntity user) {
        userId = user.getUserId();
        this.user = user;
    }
}
