package com.example.wallet.account;

import com.example.wallet.exception.InsufficientBalanceException;
import com.example.wallet.exception.InvalidAccountStateException;
import com.example.wallet.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountServiceTest {

    @Test
    void shouldCreateAccountWithZeroBalance() {
        AccountService service = new AccountService();

        Account account = service.createAccount("A-1", "Alice");

        assertEquals("Alice", account.getOwnerName());
        assertEquals(BigDecimal.ZERO, account.getBalance());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void shouldDepositAndWithdraw() {
        Account account = new Account("A-1", "Alice");

        account.deposit(new BigDecimal("100.00"));
        account.withdraw(new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), account.getBalance());
    }

    @Test
    void shouldRejectInsufficientBalance() {
        Account account = new Account("A-1", "Alice");

        account.deposit(new BigDecimal("100.00"));

        assertThrows(
                InsufficientBalanceException.class,
                () -> account.withdraw(new BigDecimal("150.00"))
        );
    }

    @Test
    void shouldRejectInvalidAmount() {
        Account account = new Account("A-1", "Alice");

        assertThrows(
                InvalidAmountException.class,
                () -> account.deposit(BigDecimal.ZERO)
        );
    }

    @Test
    void shouldRejectOperationsOnSuspendedAccount() {
        Account account = new Account("A-1", "Alice");

        account.suspend();

        assertThrows(
                InvalidAccountStateException.class,
                () -> account.deposit(new BigDecimal("100.00"))
        );
    }

    @Test
    void shouldCloseAccountWithZeroBalance() {
        Account account = new Account("A-1", "Alice");

        account.close();

        assertEquals(AccountStatus.CLOSED, account.getStatus());
    }

    @Test
    void shouldRejectClosingAccountWithNonZeroBalance() {
        Account account = new Account("A-1", "Alice");

        account.deposit(new BigDecimal("100.00"));

        assertThrows(
                InvalidAccountStateException.class,
                account::close
        );
    }
}