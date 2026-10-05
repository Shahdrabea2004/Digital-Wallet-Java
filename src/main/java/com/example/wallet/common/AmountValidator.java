package com.example.wallet.common;

import com.example.wallet.account.AccountStatus;
import com.example.wallet.exception.InvalidAmountException;

import java.math.BigDecimal;

public final class AmountValidator {

    private AmountValidator() {

    }
    public static void validate(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
