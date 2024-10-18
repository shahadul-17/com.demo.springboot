package com.demo.springboot.user;

import com.demo.springboot.address.AddressMapper;
import com.demo.springboot.core.security.cryptography.HashAlgorithm;
import com.demo.springboot.core.security.cryptography.HashProvider;
import com.demo.springboot.core.text.Encoding;
import com.demo.springboot.core.utilities.StringUtilities;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@Scope(scopeName = "singleton")
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository repository;
    @Autowired
    private CachedUserRepositoryByUserId cachedUserRepositoryByUserId;
    @Autowired
    private CachedUserRepositoryByPublicId cachedUserRepositoryByPublicId;
    @Autowired
    private CachedUserRepositoryByEmail cachedUserRepositoryByEmail;
    @Autowired
    private HashProvider hashProvider;

    @Override
    public UserEntity findByUserId(final Long userId) {
        final var optionalCachedUser = cachedUserRepositoryByUserId.findById(userId);

        if (optionalCachedUser.isPresent()) {
            logger.debug("User cache hit for userId {}", userId);

            return optionalCachedUser.get().getUser();
        }

        logger.debug("User cache missed for userId {}", userId);

        final var optionalUser = repository.findById(userId);

        if (optionalUser.isEmpty()) { return null; }

        final var userEntity = optionalUser.get();

        cachedUserRepositoryByUserId.save(new CachedUserEntityByUserId(userEntity));

        return userEntity;
    }

    @Override
    public UserEntity findByPublicId(final String publicId) {
        final var optionalCachedUser = cachedUserRepositoryByPublicId.findById(publicId);

        if (optionalCachedUser.isPresent()) {
            logger.debug("User cache hit for publicId {}", publicId);

            return optionalCachedUser.get().getUser();
        }

        logger.debug("User cache missed for publicId {}", publicId);

        final var optionalUser = repository.findByPublicId(publicId);

        if (optionalUser.isEmpty()) { return null; }

        final var userEntity = optionalUser.get();

        cachedUserRepositoryByPublicId.save(new CachedUserEntityByPublicId(userEntity));

        return userEntity;
    }

    @Cacheable
    @Override
    public UserEntity findByEmail(final String email) {
        final var optionalCachedUser = cachedUserRepositoryByEmail.findById(email);

        if (optionalCachedUser.isPresent()) {
            logger.debug("User cache hit for email {}", email);

            return optionalCachedUser.get().getUser();
        }

        logger.debug("User cache missed for email {}", email);

        final var optionalUser = repository.findByEmail(email);

        if (optionalUser.isEmpty()) { return null; }

        final var userEntity = optionalUser.get();

        cachedUserRepositoryByEmail.save(new CachedUserEntityByEmail(userEntity));

        return userEntity;
    }

    @Override
    public UserDto signup(final UserRegistrationDto registration) {
        final var password = StringUtilities.getDefaultIfNull(
                registration.getPassword(), StringUtilities.getEmptyString(), false);
        final var confirmPassword = StringUtilities.getDefaultIfNull(
                registration.getConfirmPassword(), StringUtilities.getEmptyString(), false);

        if (!password.equals(confirmPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and confirm-password does not match.");
        }

        String hashedPassword;

        try {
            hashedPassword = hashProvider.computeHash(
                    password, HashAlgorithm.SHA2_512, Encoding.BASE_64);
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }

        final var currentTime = new Date();
        var userEntity = new UserEntity();
        userEntity.setUserId(0L);
        userEntity.setPublicId(UUID.randomUUID().toString());
        userEntity.setFirstName(registration.getFirstName());
        userEntity.setLastName(registration.getLastName());
        userEntity.setEmail(registration.getEmail());
        userEntity.setHashedPassword(hashedPassword);
        userEntity.setCreatedAt(currentTime);
        // after saving it to the database, we'll get the user ID...
        userEntity = repository.saveAndFlush(userEntity);

        return UserDto.builder()
                .userId(userEntity.getPublicId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    @Override
    public UserDto login(final UserLoginDto loginDto) {
        final var userEntity = findByEmail(loginDto.getEmail());

        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user account associated with the email '" + loginDto.getEmail() + "'.");
        }

        boolean passwordMatched;

        try {
            passwordMatched = hashProvider.isMatched(loginDto.getPassword(),
                    userEntity.getHashedPassword(), HashAlgorithm.SHA2_512, Encoding.BASE_64);
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }

        if (!passwordMatched) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect password provided.");
        }

        return UserDto.builder()
                .userId(userEntity.getPublicId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .updatedAt(userEntity.getUpdatedAt())
                .build();
    }

    @Override
    public UserDto createUser(final UserDto user) {
        final var userEntity = new UserEntity();
        userEntity.setUserId(0L);
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        // userEntity.setEntityVersion(1L);
        // userEntity.setDateOfBirth(DateTimeFormatter.parseDate(user.getDateOfBirth(), "DD-MM-YYYY"));

        final var addresses = user.getAddresses();

        // if addresses are present...
        if (addresses != null && !addresses.isEmpty()) {
            userEntity.setAddresses(AddressMapper.INSTANCE.fromDto(addresses));

            /*final Set<AddressEntity> addressEntities = new HashSet<>();

            for (final var address : addresses) {
                final var addressEntity = new AddressEntity();
                addressEntity.setAddressId(address.getAddressId());
                addressEntity.setEntityVersion(address.getEntityVersion());
                addressEntity.setCity(address.getCity());
                addressEntity.setCountry(address.getCountry());

                addressEntities.add(addressEntity);
            }

            userEntity.setAddresses(addressEntities);*/
        }

        final var savedUserEntity = repository.saveAndFlush(userEntity);
        final var savedAddressEntities = savedUserEntity.getAddresses();

        user.setUserId(savedUserEntity.getPublicId());
        // user.setEntityVersion(savedUserEntity.getEntityVersion());

        if (savedAddressEntities != null && !savedAddressEntities.isEmpty()) {
            user.setAddresses(AddressMapper.INSTANCE.toDto(savedAddressEntities));

            /*final Set<AddressDto> _addresses = new HashSet<>();

            for (final var addressEntity : savedAddressEntities) {
                _addresses.add(AddressDto.builder()
                        .addressId(addressEntity.getAddressId())
                        .entityVersion(addressEntity.getEntityVersion())
                        .city(addressEntity.getCity())
                        .country(addressEntity.getCountry())
                        .build());
            }

            user.setAddresses(_addresses);*/
        }

        return user;
    }

    @Override
    public UserDto updateUser(final UserDto user) {
        final var userEntity = new UserEntity();
        userEntity.setPublicId(user.getUserId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        // userEntity.setEntityVersion(user.getEntityVersion());
        // userEntity.setDateOfBirth(DateTimeFormatter.parseDate(user.getDateOfBirth(), "DD-MM-YYYY"));

        final var savedUserEntity = repository.saveAndFlush(userEntity);

        user.setUserId(savedUserEntity.getPublicId());
        // user.setEntityVersion(savedUserEntity.getEntityVersion());

        return user;
    }
}
