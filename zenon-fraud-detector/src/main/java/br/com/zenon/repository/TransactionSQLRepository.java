package br.com.zenon.repository;

import br.com.zenon.ConnectionFactory;
import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionCustomer;
import br.com.zenon.model.TransactionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository{
    @Override
    public void save(Transaction transaction) {
        String sql = """
                insert into zenon_frauds.transactions
                    (step, `type`, amount, name_origin, old_balance_origin, new_balance_origin, name_recipient, old_balance_recipient, new_balance_recipient, is_fraud, is_flagged_fraud)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1,transaction.step());
            ps.setString(2, transaction.type().name());
            ps.setBigDecimal(3, transaction.amount());

            ps.setString(4, transaction.origin().name());
            ps.setBigDecimal(5, transaction.origin().oldBalance());
            ps.setBigDecimal(6, transaction.origin().newBalance());

            ps.setString(7, transaction.recipient().name());
            ps.setBigDecimal(8, transaction.recipient().oldBalance());
            ps.setBigDecimal(9, transaction.recipient().newBalance());

            ps.setBoolean(10, transaction.isFraud());
            ps.setBoolean(11, transaction.isFlaggedFraud());

            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar nova transação", e);
        }
    }

    @Override
    public Optional<Transaction> findByOriginName(String originName) {
        String sql = """
                SELECT id, step, `type`, amount, 
                       name_origin, old_balance_origin, new_balance_origin, 
                       name_recipient, old_balance_recipient, new_balance_recipient, 
                       is_fraud, is_flagged_fraud
                FROM zenon_frauds.transactions
                WHERE name_origin = ?
                ORDER BY step
                LIMIT 1
                """;

        try (Connection connection = ConnectionFactory.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, originName);
            try(ResultSet resultSet = ps.executeQuery()){
                if(resultSet.next()){
                    Transaction transaction = mapResultSetToTransaction(resultSet);
                    return Optional.of(transaction);
                } else {
                    System.out.println("Transação não encontrada para origin: " + originName);
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar transação da origin: " + originName, e);
        }

    }

    private Transaction mapResultSetToTransaction(ResultSet resultSet) {
        try {
            int step = resultSet.getInt("step");
            TransactionType type = TransactionType.valueOf(resultSet.getString("type"));
            BigDecimal amount = resultSet.getBigDecimal("amount");

            String originName = resultSet.getString("name_origin");
            BigDecimal originOldBalance = resultSet.getBigDecimal("old_balance_origin");
            BigDecimal originNewBalance = resultSet.getBigDecimal("new_balance_origin");

            TransactionCustomer origin = new TransactionCustomer(originName, originOldBalance, originNewBalance);

            String recipientName = resultSet.getString("name_recipient");
            BigDecimal recipientOldBalance = resultSet.getBigDecimal("old_balance_recipient");
            BigDecimal recipientNewBalance = resultSet.getBigDecimal("new_balance_recipient");

            TransactionCustomer recipient = new TransactionCustomer(recipientName, recipientOldBalance, recipientNewBalance);

            boolean isFraud = resultSet.getBoolean("is_fraud");
            boolean isFlaggedFraud = resultSet.getBoolean("is_flagged_fraud");

            return new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
