package com.demo.springboot.ping;

import com.demo.springboot.core.utilities.StringUtilities;
import com.demo.springboot.core.utilities.ThreadUtilities;
import com.demo.springboot.security.UserDetails;
import com.demo.springboot.user.UserEntity;
import com.demo.springboot.user.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@RestController
@RequestMapping(path = "v{version}/ping")
public class PingController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String REDIS_KEY_PREFIX = "REDIS_";

    @GetMapping
    public ResponseEntity<PingDto> ping(
            @PathVariable final String version,
            @AuthenticationPrincipal UserEntity userEntity) {
        if (!"1.0".equals(version)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported version (" + version + ") provided.");
        }

        System.out.println(userEntity.toString());

        final var x = userService.findByCreationDateEx(new Date(userEntity.getCreatedAt().getTime() - 90_000));

        System.out.println(x.isEmpty() ? "USER NOT PRESENT" : x + " ----USER PRESENT----------");

        redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + "hello", "World");

        final var virtualThreadName = Thread.currentThread().isVirtual()
                ? Thread.currentThread().getName()
                : StringUtilities.getEmptyString();
        final var pingDto = PingDto.builder()
                .version(version)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully processed ping request.")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("X-Current-Virtual-Thread", virtualThreadName)
                .header("X-Current-Platform-Thread", ThreadUtilities.getCurrentPlatformThreadName())
                .body(pingDto);
    }

}
