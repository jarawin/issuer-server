package com.jarawin.issuer.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarawin.issuer.entity.Transaction;
import com.jarawin.issuer.repository.TransactionRepository;
import com.jarawin.issuer.util.TransactionUtils;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction saveTransaction(Transaction transaction) {
        if (transaction.getCreateDate() == null) {
            transaction.setCreateDate(LocalDate.now());
            transaction.setCreateTime(LocalTime.now());
        }
        if (transaction.getTransactionId() == null) {
            String id = TransactionUtils.generateTransactionId();
            transaction.setTransactionId(id);
        }

        return transactionRepository.save(transaction);
    }

    public List<Transaction> saveAllTransactions(List<Transaction> transactions) {
        return (List<Transaction>) transactionRepository.saveAll(transactions);
    }

    public Transaction updateTransaction(Transaction oldData, Transaction newData) {
        if (newData.getAmount() != null) {
            oldData.setAmount(newData.getAmount());
        }
        if (newData.getCardNumber() != null) {
            oldData.setCardNumber(newData.getCardNumber());
        }
        if (newData.getAcquirerId() != null) {
            oldData.setAcquirerId(newData.getAcquirerId());
        }
        if (newData.getRequestId() != null) {
            oldData.setRequestId(newData.getRequestId());
        }
        if (newData.getStatus() != null) {
            oldData.setStatus(newData.getStatus());
        }

        return transactionRepository.save(oldData);
    }

    public List<Transaction> findAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public Transaction findTransactionByTransactionIdAndRequestId(
            String transactionId, String requestId) {
        return transactionRepository.findByTransactionIdAndRequestId(transactionId, requestId);
    }

    public Transaction findTransactionByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }

    public Transaction findTransactionByRequestId(String requestId) {
        return transactionRepository.findByRequestId(requestId);
    }

    public List<Transaction> findAllTransactionsByAcquirerIdAndStatus(
            String acquirerId, String status) {
        return transactionRepository.findAllByAcquirerIdAndStatus(acquirerId, status);
    }

    public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }
}