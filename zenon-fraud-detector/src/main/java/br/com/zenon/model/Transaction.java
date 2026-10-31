package br.com.zenon.model;

import java.math.BigDecimal;
import java.util.Objects;

public record Transaction(

        int step,
        TransactionType type,
        BigDecimal amount,
        TransactionCustomer origin,
        TransactionCustomer recipient,
        boolean isFraud,
        boolean isFlaggedFraud
) {
    public Transaction{
        Objects.requireNonNull(type);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(recipient);

        if (step <= 0)
            throw new IllegalArgumentException("O valor de step deve ser positivo!");
        if (amount.signum() < 0)
            throw new IllegalArgumentException("O valor de amount deve ser maior ou igual a zero!");
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "step=" + step +
                ", type=" + type +
                ", amount=" + amount +
                ", origin=" + origin +
                ", recipient=" + recipient +
                ", isFraud=" + isFraud +
                ", isFlaggedFraud=" + isFlaggedFraud +
                '}';
    }
}
