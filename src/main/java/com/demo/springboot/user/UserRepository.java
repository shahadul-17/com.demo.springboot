package com.demo.springboot.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(final String email);
    Optional<UserEntity> findByPublicId(final String publicId);

    @Query("SELECT u FROM UserEntity u WHERE u.createdAt > ?1")
    Optional<UserEntity> findByCreationDateEx(final Date createdAt);
}
