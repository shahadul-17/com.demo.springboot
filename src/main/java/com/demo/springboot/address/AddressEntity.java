package com.demo.springboot.address;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
public class AddressEntity {

    @Id
    @GeneratedValue
    @Column(name = "address_id", unique = true)
    private Long addressId;

    @Version
    @Column(name = "entity_version", nullable = false)
    private Long entityVersion = 0L;

    @Column(name = "city", length = 64)
    private String city;

    @Column(name = "country", length = 64)
    private String country;
}
