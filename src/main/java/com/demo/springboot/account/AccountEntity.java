package com.demo.springboot.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(AccountId.class)
public class AccountEntity {

    @Id
    private String accountNumber;

    @Id
    private String accountType;
}
