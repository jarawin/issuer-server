
package com.jarawin.issuer.controller;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jarawin.issuer.entity.Transaction;
import com.jarawin.issuer.service.TransactionService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    // Read all
    @GetMapping()
    public List<Transaction> getTransactions() {
        return transactionService.findAllTransactions();
    }

    // Read one by tran id and req id
    @GetMapping("/transaction-request-id")
    public ResponseEntity<?> getTransaction(
            @RequestParam(name = "transactionId") String transactionId,
            @RequestParam(name = "requestId") String requestId) {
        Transaction transaction = transactionService
                .findTransactionByTransactionIdAndRequestId(transactionId, requestId);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);
    }

    // Read one by tran id
    @GetMapping("/transaction-id/{transactionId}")
    public ResponseEntity<?> getTransactionByTransactionId(@PathVariable String transactionId) {
        Transaction transaction = transactionService
                .findTransactionByTransactionId(transactionId);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);
    }

    // Read one by req id
    @GetMapping("/request-id/{requestId}")
    public ResponseEntity<?> getTransactionByRequestId(@PathVariable String requestId) {
        Transaction transaction = transactionService.findTransactionByRequestId(requestId);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);
    }

    // Create
    @PostMapping()
    public ResponseEntity<?> postTransaction(@RequestBody Transaction body) {
        Transaction transaction = transactionService.saveTransaction(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    // Update
    @PutMapping()
    public ResponseEntity<?> putTransaction(
            @RequestParam(name = "transactionId") String transactionId,
            @RequestParam(name = "requestId") String requestId,
            @Valid @RequestBody Transaction body) {

        Transaction transaction = transactionService
                .findTransactionByTransactionIdAndRequestId(transactionId, requestId);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        transaction = transactionService.updateTransaction(transaction, body);
        return ResponseEntity.ok(transaction);
    }

    // Delete
    @DeleteMapping()
    public ResponseEntity<?> deleteTransaction(
            @RequestParam(name = "transactionId") String transactionId,
            @RequestParam(name = "requestId") String requestId) {

        Transaction transaction = transactionService
                .findTransactionByTransactionIdAndRequestId(transactionId, requestId);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        transactionService.deleteTransaction(transaction);
        return ResponseEntity.ok().build();
    }
}