package br.com.zenon.repository;

import br.com.zenon.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByOriginName(String originName);
}
