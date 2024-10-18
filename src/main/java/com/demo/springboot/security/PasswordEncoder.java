package com.demo.springboot.security;

import com.demo.springboot.core.security.cryptography.HashAlgorithm;
import com.demo.springboot.core.security.cryptography.HashProvider;
import com.demo.springboot.core.text.Encoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder implements org.springframework.security.crypto.password.PasswordEncoder {

    @Autowired
    private HashProvider hashProvider;

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return hashProvider.computeHash((String) rawPassword, HashAlgorithm.SHA3_512, Encoding.URL_SAFE_BASE_64);
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return hashProvider.isMatched((String) rawPassword, encodedPassword, HashAlgorithm.SHA3_512, Encoding.URL_SAFE_BASE_64);
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
