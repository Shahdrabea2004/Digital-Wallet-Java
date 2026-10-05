package com.example.wallet.transaction;

import com.example.wallet.common.AmountValidator;
import com.example.wallet.exception.DuplicateTransactionException;

import java.math.BigDecimal;
import java.util.*;

public class TransactionService {

    private final List<Transaction> transactions = new ArrayList<>();

    private final Set<String> processedRequestIds = new HashSet<>();


    public Transaction record(String accountId, TransactionType type, BigDecimal amount) {
        // TODO: validate input and prevent duplicate processing where appropriate.
        validateTransactionData(accountId, type, amount);
        Transaction transaction = new Transaction(accountId, type, amount);
        transactions.add(transaction);
        return transaction;
    }

    //prevent duplicate processing where appropriate.
    public Transaction record(String requestId, String accountId, TransactionType type, BigDecimal amount) {

        validateRequestId(requestId);

        return record(accountId, type, amount);
    }


    public List<Transaction> findByAccountId(String accountId) {
        // TODO: avoid leaking mutable internal state and return transactions for the account.
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("accountId must not be blank");
        }
        return Collections.unmodifiableList(
                transactions.stream()
                        .filter(t -> t.getAccountId().equals(accountId))
                        .toList()
        );
    }

    private void validateTransactionData(String accountId, TransactionType type, BigDecimal amount) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("accountId must not be blank");
        }

        Objects.requireNonNull(type, "type must not be null");

        AmountValidator.validate(amount);
    }

    private void validateRequestId(String requestId) {

        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("requestId must not be blank");
        }

        if (!processedRequestIds.add(requestId)) {
            throw new DuplicateTransactionException(
                    "Request already processed: " + requestId);
        }
    }
}