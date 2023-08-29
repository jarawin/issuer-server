package com.jarawin.issuer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jarawin.issuer.dto.composite.TransactionRequestId;
import com.jarawin.issuer.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionRequestId> {
    Transaction findByTransactionIdAndRequestId(String transactionId, String requestId);

    Transaction findByTransactionId(String transactionId);

    Transaction findByRequestId(String requestId);

    @Query("SELECT t FROM Transaction t WHERE t.acquirerId = :acquirerId AND t.status = :status")

    List<Transaction> findAllByAcquirerIdAndStatus(@Param("acquirerId") String acquirerId,
            @Param("status") String status);
}
