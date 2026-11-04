package br.com.zenon;

import br.com.zenon.TransactionReport.Statistics;

public class ReportMain {
    static void main() {
        TransactionReport transactionReport = new TransactionReport();
        Statistics statistics = transactionReport.generateReport("data\\PS_20174392719_1491204439457_log.csv");
        System.out.printf("""
                Total de transações: %d
                Total de fraudes: %d
                Valor total transacionado: %.2f
                %n""", statistics.totalTransactions(), statistics.totalFrauds(), statistics.totalAmount());

    }
}
