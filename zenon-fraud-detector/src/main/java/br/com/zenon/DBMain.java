package br.com.zenon;

import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionCustomer;
import br.com.zenon.model.TransactionType;
import br.com.zenon.repository.TransactionSQLRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class DBMain {
    static void main() {
        Connection connection = ConnectionFactory.getConnection();
        System.out.println("Conexão com o BD criada!");

        TransactionSQLRepository repository = new TransactionSQLRepository();

        TransactionIngestor transactionIngestor = new TransactionIngestor();


        long starTimeSQL = System.nanoTime();
        List<Transaction> transactions = transactionIngestor.read("data\\PS_20174392719_1491204439457_log.csv");
        System.out.println(transactions.size());
        System.out.println("Iniciando adição das transações no BD...");

        transactions.forEach(repository::save);

        long endTimeSQL = System.nanoTime();
        System.out.println("Tempo de inserção - SQL (ms): " + (endTimeSQL - starTimeSQL)/1_000_000.0);

        repository.findByOriginName("C1231006815")
                .ifPresentOrElse(System.out::println, ()-> System.out.println("Transação não encontrada para C1231006815"));

    }
}
