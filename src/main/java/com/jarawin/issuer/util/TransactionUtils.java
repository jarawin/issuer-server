package com.jarawin.issuer.util;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.jarawin.issuer.entity.Transaction;

public class TransactionUtils {

    public static String generateTransactionId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().toUpperCase().replace("-", "");
    }

    public static double calculateTotalAmount(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static void updateAllStatus(List<Transaction> transactions, String status) {
        for (Transaction transaction : transactions) {
            transaction.setStatus(status);
        }
    }

    public static List<Transaction> getMatchingTransactions(
            List<Transaction> transactions1, List<Transaction> transactions2) {

        List<Transaction> transactions3 = transactions1.stream()
                .filter(transaction1 -> transactions2.stream()
                        .anyMatch(
                                transaction2 -> transaction2.getTransactionId().equals(transaction1.getTransactionId())
                                        &&
                                        transaction2.getRequestId().equals(transaction1.getRequestId())))
                .collect(Collectors.toList());

        return transactions3;
    }

    public static boolean isTransactionMatchAmount(
            List<Transaction> transactions1, List<Transaction> transactions2) {

        if (transactions1.size() != transactions2.size()) {
            return false;
        }

        for (int i = 0; i < transactions1.size(); i++) {
            Transaction transaction1 = transactions1.get(i);
            Transaction transaction2 = transactions2.get(i);

            if (!transaction1.getTransactionId().equals(transaction2.getTransactionId()) ||
                    !transaction1.getAmount().equals(transaction2.getAmount())) {
                return false;
            }
        }

        return true;
    }
}
