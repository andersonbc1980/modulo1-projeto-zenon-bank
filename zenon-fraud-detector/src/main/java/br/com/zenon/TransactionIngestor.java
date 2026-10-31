package br.com.zenon;

import br.com.zenon.model.Transaction;
import br.com.zenon.model.TransactionCustomer;
import br.com.zenon.model.TransactionType;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TransactionIngestor {

    /*Forma mais antiga de ler um arquivo
    public List<Transaction> readOldForm(String fileName){
        List<Transaction> transactions = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(fileName)){
            Scanner scanner = new Scanner(fis);
            int lineCount = 0;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                lineCount++;

                if(lineCount == 1){
                    continue;
                }
                if(lineCount > 1001){
                    break;
                }

                Transaction transaction = parseTransaction(line);
                transactions.add(transaction);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo: " + fileName, e);
        }
        return transactions;
    }

    //Forma que eu fiz
    public List<Transaction> readMyWay(String fileName) throws IOException {
        List<Transaction> list = new ArrayList<>();

        Path file = Path.of("C:\\Projetos Pos Java\\modulo1-projeto-zenon-bank\\zenon-fraud-detector\\data\\" + fileName);
        String fileContent = Files.readString(file);
        String[] fileLines = fileContent.split("\r?\n");

        for (int i = 1; i < 1001; i++) {
            String line = fileLines[i];

            Transaction transaction = parseTransaction(line);
            list.add(transaction);
        }
        return list;
    }*/

    //Forma mais atual de ler um arquivo
    public List<Transaction> read(String fileName){
        Path path = Path.of(fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(1000)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (Exception e){
            throw new RuntimeException("Erro ao ler o arquivo: " + fileName, e);
        }

    }

    private Optional<Transaction> parseTransaction(String line) {
        try {
            String[] linePart = line.split(",");

            int step = Integer.parseInt(linePart[0]);
            TransactionType type = TransactionType.valueOf(linePart[1]);
            if (linePart[2] == null || linePart[2].trim().isEmpty())
                throw new IllegalArgumentException("O valor de amount não pode ser nulo ou vazio!");
            BigDecimal amount = new BigDecimal(linePart[2]);
            TransactionCustomer origin =
                    new TransactionCustomer(linePart[3], new BigDecimal(linePart[4]), new BigDecimal(linePart[5]));
            TransactionCustomer recipient =
                    new TransactionCustomer(linePart[6], new BigDecimal(linePart[7]), new BigDecimal(linePart[8]));
            boolean isFraud = linePart[9].equals("1");
            boolean isFlaggedFraud = linePart[10].equals("1");

            return Optional.of(new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud));
        } catch (Exception e){
            System.err.println("Erro ao fazer parse: " + line + " | " + e);
            return Optional.empty();
        }
    }
}
