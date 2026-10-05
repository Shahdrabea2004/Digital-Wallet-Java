package com.example.wallet.transaction;

import com.example.wallet.exception.DuplicateTransactionException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionServiceTest {

    @Test
    void shouldRecordAndFindTransactionsByAccount() {
        TransactionService service = new TransactionService();

        service.record("A-1", TransactionType.DEPOSIT, new BigDecimal("100.00"));
        service.record("A-2", TransactionType.DEPOSIT, new BigDecimal("50.00"));

        assertEquals(1, service.findByAccountId("A-1").size());
        assertEquals(new BigDecimal("100.00"), service.findByAccountId("A-1").get(0).getAmount());
    }

    @Test
    void shouldRejectDuplicateRequestId() {
        TransactionService service = new TransactionService();

        String requestId = "REQ-123";

        service.record(
                requestId,
                "A-1",
                TransactionType.DEPOSIT,
                new BigDecimal("100.00")
        );

        assertThrows(
                DuplicateTransactionException.class,
                () -> service.record(
                        requestId,
                        "A-1",
                        TransactionType.DEPOSIT,
                        new BigDecimal("100.00")
                )
        );
    }

    @Test
    void shouldRejectBlankRequestId() {
        TransactionService service = new TransactionService();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.record(
                        "",
                        "A-1",
                        TransactionType.DEPOSIT,
                        new BigDecimal("100.00")
                )
        );
    }
}