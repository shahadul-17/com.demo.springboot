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
@RedisHash
public class CachedUserEntityByPublicId {

    @Id
    private String publicId;
    private UserEntity user;

    public CachedUserEntityByPublicId(final UserEntity user) {
        publicId = user.getPublicId();
        this.user = user;
    }
}
