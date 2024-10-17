package com.demo.springboot.account;

import com.demo.springboot.core.utilities.StringUtilities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountId implements Serializable {

    private String accountNumber;
    private String accountType;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountId accountId)) { return false; }

        final var accountA = StringUtilities.getEmptyString() + getAccountNumber() + getAccountType();
        final var accountB = StringUtilities.getEmptyString() + accountId.getAccountNumber() + accountId.getAccountType();

        return accountA.equals(accountB);
    }

    @Override
    public int hashCode() {
        final var account = StringUtilities.getEmptyString() + getAccountNumber() + getAccountType();

        return account.hashCode();
    }
}
