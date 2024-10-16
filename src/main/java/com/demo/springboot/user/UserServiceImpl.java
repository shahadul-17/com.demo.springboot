package com.demo.springboot.user;

import com.demo.springboot.core.utilities.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = "singleton")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDto createUser(final UserDto user) {
        final var userEntity = new UserEntity();
        userEntity.setUserId(0L);
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEntityVersion(1L);
        userEntity.setDateOfBirth(DateTimeFormatter.parseDate(user.getDateOfBirth(), "DD-MM-YYYY"));

        final var savedUserEntity = repository.saveAndFlush(userEntity);

        user.setUserId(savedUserEntity.getUserId());
        user.setEntityVersion(savedUserEntity.getEntityVersion());

        return user;
    }

    @Override
    public UserDto updateUser(final UserDto user) {
        final var userEntity = new UserEntity();
        userEntity.setUserId(user.getUserId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEntityVersion(user.getEntityVersion());
        userEntity.setDateOfBirth(DateTimeFormatter.parseDate(user.getDateOfBirth(), "DD-MM-YYYY"));

        final var savedUserEntity = repository.saveAndFlush(userEntity);

        user.setUserId(savedUserEntity.getUserId());
        user.setEntityVersion(savedUserEntity.getEntityVersion());

        return user;
    }
}
