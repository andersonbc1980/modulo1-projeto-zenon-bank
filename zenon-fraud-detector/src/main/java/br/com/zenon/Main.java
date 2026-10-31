package br.com.zenon;

import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionCustomer;
import br.com.zenon.model.TransactionType;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    static void main() throws IOException {
        Transaction transaction1 = new Transaction(1, TransactionType.PAYMENT, new BigDecimal("9839.64"),
                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
                false, false);
        Transaction transaction2 = new Transaction(743, TransactionType.CASH_OUT, new BigDecimal("850002.52"),
                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true, false);

        System.out.println(transaction1);
        System.out.println(transaction2);

       /* System.out.println();
        System.out.println("---- Forma mais antiga de ler arquivo ----");
        System.out.println();
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.readOldForm("data\\PS_20174392719_1491204439457_log.csv");

        transactions.stream().limit(10).forEach(System.out::println);

        System.out.println();
        System.out.println("---- Forma que eu fiz de ler arquivo ----");
        System.out.println();
        String fileName = IO.readln("Digite o nome do arquivo: ");
        //TransactionIngestor trasactionIngestor = new TransactionIngestor();
        List<Transaction> transaction3 = transactionIngestor.readMyWay(fileName);
        for (int i = 0; i < 10; i++) {
            System.out.println(transaction3.get(i));
        }*/

        System.out.println();
        System.out.println("---- Forma mais atual de ler arquivo ----");
        System.out.println();
        TransactionIngestor transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.read("data\\PS_20174392719_1491204439457_log.csv");

        transactions.stream().limit(10).forEach(System.out::println);

        System.out.println();
        System.out.println("---- Arquivo bad dat ----");
        System.out.println();

        List<Transaction> transactions2 = transactionIngestor.read("data\\paysim_with_bad_data.csv");

        transactions2.stream().forEach(System.out::println);


    }
}

