package com.demo.springboot.user;

import com.demo.springboot.address.AddressEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

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

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.DETACH,
                    CascadeType.REFRESH,
            })
    @JoinTable(
            name = "USER_ADDRESS_MAPPING",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private Set<AddressEntity> addresses;
}
