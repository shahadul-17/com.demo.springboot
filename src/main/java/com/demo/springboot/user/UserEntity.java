package com.demo.springboot.user;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "user_entity")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId = 0L;

    @Version
    @Column(name = "entity_version", nullable = false)
    private Long entityVersion = 0L;

    @Column(name = "first_name", length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = true)
    private String lastName;

    @Column(nullable = true)
    private Date dateOfBirth;
}
