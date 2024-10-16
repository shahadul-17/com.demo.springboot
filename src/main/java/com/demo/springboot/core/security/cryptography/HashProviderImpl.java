package com.demo.springboot.core.security.cryptography;

import com.demo.springboot.core.text.Encoder;
import com.demo.springboot.core.text.Encoding;
import com.demo.springboot.core.utilities.CollectionUtilities;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
@Scope(scopeName = "singleton")
public class HashProviderImpl implements HashProvider {

    HashProviderImpl() { }

    @Override
    public byte[] computeHash(byte[] bytes, HashAlgorithm algorithm) throws Exception {
        // creates an instance of message digest...
        var messageDigest = createMessageDigest(algorithm);
        // updates the digest...
        messageDigest.update(bytes);

        // computes the hash and performs any final operations necessary (e.g. padding)...
        var computedHash = messageDigest.digest();

        return computedHash;
    }

    @Override
    public byte[] computeHash(String message, HashAlgorithm algorithm) throws Exception {
        // converts the message into an array of bytes...
        var messageAsByteArray = message.getBytes(StandardCharsets.UTF_8);

        // computes hash of the message...
        return computeHash(messageAsByteArray, algorithm);
    }

    @Override
    public String computeHash(String message, HashAlgorithm algorithm, Encoding encoding) throws Exception {
        // computes hash of the message...
        var computedHash = computeHash(message, algorithm);
        // encodes the computed hash using the specified encoding...
        var encodedComputedHash = Encoder.encode(computedHash, encoding);

        // returns the encoded hash...
        return encodedComputedHash;
    }

    @Override
    public boolean isMatched(byte[] bytes, byte[] preComputedHashAsBytes, HashAlgorithm algorithm) throws Exception {
        // computes hash of the message...
        var computedHash = computeHash(bytes, algorithm);
        // checks if the recently computed hash is equal to the pre-computed hash...
        var matched = CollectionUtilities.sequenceEqual(computedHash, preComputedHashAsBytes);

        return matched;
    }

    @Override
    public boolean isMatched(String message, byte[] preComputedHashAsBytes, HashAlgorithm algorithm) throws Exception {
        // computes hash of the message...
        var computedHash = computeHash(message, algorithm);
        // checks if the recently computed hash is equal to the pre-computed hash...
        var matched = CollectionUtilities.sequenceEqual(computedHash, preComputedHashAsBytes);

        return matched;
    }

    @Override
    public boolean isMatched(String message, String preComputedHash,
                             HashAlgorithm algorithm, Encoding preComputedHashEncoding) throws Exception {
        // computes hash of the message...
        var computedHash = computeHash(message, algorithm);
        // decodes the pre-computed hash...
        var preComputedHashAsBytes = Encoder.decode(preComputedHash, preComputedHashEncoding);
        // checks if the recently computed hash is equal to the pre-computed hash...
        var matched = CollectionUtilities.sequenceEqual(computedHash, preComputedHashAsBytes);

        return matched;
    }

    private static MessageDigest createMessageDigest(HashAlgorithm algorithm) throws Exception {
        // creates a new instance of message digest by algorithm name...
        var messageDigest = MessageDigest.getInstance(algorithm.getName());

        // returns the message digest...
        return messageDigest;
    }
}
