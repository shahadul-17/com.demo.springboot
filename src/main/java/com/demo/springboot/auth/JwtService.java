package com.demo.springboot.auth;

import com.demo.springboot.core.text.Encoder;
import com.demo.springboot.core.text.Encoding;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationInMilliseconds;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Collections.emptyMap(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, expirationInMilliseconds);
    }

    public long getExpirationTime() {
        return expirationInMilliseconds;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final var claims = extractClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractClaims(final String token) {
        final var secretKey = SecretKeyImpl.from(this.secretKey);

        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expirationInMilliseconds
    ) {
        final var secretKey = SecretKeyImpl.from(this.secretKey);

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationInMilliseconds))
                .signWith(secretKey)
                .compact();
    }

    @Getter
    @Setter
    public static class SecretKeyImpl implements SecretKey {

        private String algorithm;
        private String format;
        private byte[] encoded;

        private static final String DEFAULT_ALGORITHM = "HmacSHA512";
        private static final String DEFAULT_FORMAT = "RAW";

        private SecretKeyImpl() { }

        public static SecretKey from(final String key, final String algorithm, final String format) {
            final var secretKey = new SecretKeyImpl();
            secretKey.setAlgorithm(algorithm);
            secretKey.setFormat(format);
            secretKey.setEncoded(Encoder.decode(key, Encoding.HEX));

            return secretKey;
        }

        public static SecretKey from(final String key, final String algorithm) {
            return from(key, algorithm, DEFAULT_FORMAT);
        }

        public static SecretKey from(final String key) {
            return from(key, DEFAULT_ALGORITHM);
        }
    }
}
