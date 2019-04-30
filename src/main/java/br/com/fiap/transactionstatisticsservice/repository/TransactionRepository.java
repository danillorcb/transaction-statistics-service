package br.com.fiap.transactionstatisticsservice.repository;

import br.com.fiap.transactionstatisticsservice.dto.TransactionDTO;
import br.com.fiap.transactionstatisticsservice.exception.MoreThan60SecException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TransactionRepository {

    private static Map<Long, TransactionDTO> transactions = new HashMap<>();

    public void add(TransactionDTO transaction) throws MoreThan60SecException {
        Long systemTimestamp = System.currentTimeMillis();

        System.out.println("systemTimestamp: " + systemTimestamp);
        System.out.println("transaction.getTimestamp(): " + transaction.getTimestamp());
        System.out.println("Sub: " + (systemTimestamp - transaction.getTimestamp()));

        if (systemTimestamp - transaction.getTimestamp() > 60000) {
            throw new MoreThan60SecException();
        }

        transactions.put(transaction.getTimestamp(), transaction);
    }

    public Map<Long, TransactionDTO> getTransactionsIn60s() {
        Long systemTimestamp = System.currentTimeMillis();
        Map<Long, TransactionDTO> transactionsIn60s = new HashMap<>();

        for (Map.Entry<Long, TransactionDTO> pair : transactions.entrySet()) {
            if (systemTimestamp - pair.getKey() <= 60000) {
                transactionsIn60s.put(pair.getKey(), pair.getValue());
            }
        }

        return transactionsIn60s;
    }
}
