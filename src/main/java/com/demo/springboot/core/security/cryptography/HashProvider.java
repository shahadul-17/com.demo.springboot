package com.demo.springboot.core.security.cryptography;

import com.demo.springboot.core.text.Encoding;

public interface HashProvider {

    byte[] computeHash(byte[] bytes, HashAlgorithm algorithm) throws Exception;

    byte[] computeHash(String message, HashAlgorithm algorithm) throws Exception;

    String computeHash(String message, HashAlgorithm algorithm, Encoding encoding) throws Exception;

    boolean isMatched(byte[] bytes, byte[] preComputedHashAsBytes, HashAlgorithm algorithm) throws Exception;

    boolean isMatched(String message, byte[] preComputedHashAsBytes, HashAlgorithm algorithm) throws Exception;

    boolean isMatched(String message, String preComputedHash, HashAlgorithm algorithm, Encoding preComputedHashEncoding) throws Exception;
}
