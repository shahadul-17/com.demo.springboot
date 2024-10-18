package com.demo.springboot.user;

import com.demo.springboot.address.AddressEntity;
import com.demo.springboot.core.utilities.StringUtilities;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    // this is the public facing user ID...
    @Column(name = "public_id", unique = true, nullable = false)
    private String publicId = StringUtilities.getEmptyString();

    @Column(name = "first_name", length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = true)
    private String lastName;

    @Column(length = 512, nullable = false, unique = true)
    private String email;

    @Column(name = "hashed_password", length = 128, nullable = false, unique = false)
    private String hashedPassword;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Date createdAt;

    @Version
    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

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
