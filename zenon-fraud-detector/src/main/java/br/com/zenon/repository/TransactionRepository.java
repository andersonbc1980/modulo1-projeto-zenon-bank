package br.com.zenon.repository;

import br.com.zenon.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    void save(Transaction transaction);
    Optional<Transaction> findByOriginName(String originName);

}
