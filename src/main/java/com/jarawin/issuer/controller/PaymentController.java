package com.jarawin.issuer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.jarawin.issuer.dto.payment.request.RequestPaymentBatchUpload;
import com.jarawin.issuer.dto.payment.request.RequestPaymentInquiry;
import com.jarawin.issuer.dto.payment.request.RequestPaymentRefund;
import com.jarawin.issuer.dto.payment.request.RequestPaymentReverse;
import com.jarawin.issuer.dto.payment.request.RequestPaymentSale;
import com.jarawin.issuer.dto.payment.request.RequestPaymentSettlement;
import com.jarawin.issuer.dto.payment.request.RequestPaymentVoid;
import com.jarawin.issuer.dto.payment.response.ResponsePayment;
import com.jarawin.issuer.dto.payment.response.ResponsePaymentInquiry;
import com.jarawin.issuer.dto.payment.response.ResponsePaymentSale;
import com.jarawin.issuer.dto.payment.response.data.DataPaymentInquiry;
import com.jarawin.issuer.dto.payment.response.data.DataPaymentSale;
import com.jarawin.issuer.entity.Acquirer;
import com.jarawin.issuer.entity.Card;
import com.jarawin.issuer.entity.Transaction;
import com.jarawin.issuer.service.AcquirerService;
import com.jarawin.issuer.service.CardService;
import com.jarawin.issuer.service.TransactionService;
import com.jarawin.issuer.util.TransactionUtils;

@RestController
@RequestMapping("/payments")
public class PaymentController {
        @Autowired
        CardService cardService;

        @Autowired
        AcquirerService acquirerService;

        @Autowired
        TransactionService transactionService;

        // Inquiry
        @PostMapping("/inquiry")
        public ResponseEntity<?> paymentInquiry(@RequestBody RequestPaymentInquiry body) {
                Transaction transaction;

                // check acquirerId is not null
                if (body.getAcquirerId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId is required")
                                        .build());
                }

                // check transactionId or requestId is not null
                if (body.getTransactionId() == null || body.getRequestId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("TransactionId or RequestId is required")
                                        .build());
                }

                // check acquirer is not null
                Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
                if (acquirer == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer not found")
                                        .build());
                }

                // check acquirer status is not active
                if (!acquirer.getStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer is not active")
                                        .build());
                }

                // find transaction
                if (body.getRequestId() == null) {
                        transaction = transactionService.findTransactionByTransactionId(body.getTransactionId());
                } else if (body.getTransactionId() == null) {
                        transaction = transactionService.findTransactionByRequestId(body.getRequestId());
                } else {
                        transaction = transactionService.findTransactionByTransactionIdAndRequestId(
                                        body.getTransactionId(),
                                        body.getRequestId());
                }

                // check transaction is not null
                if (transaction == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Transaction not found")
                                        .build());
                }

                // check card is not null
                Card card = cardService.findCardByNumber(transaction.getCardNumber());
                if (card == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card not found")
                                        .build());
                }

                // check card status is not active
                if (!card.getCardStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card is not active")
                                        .build());
                }

                // check req and trans does match with acquirerId
                if (!transaction.getAcquirerId().equals(body.getAcquirerId())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId does not match")
                                        .build());
                }

                // create response
                DataPaymentInquiry responsePaymentInquiry = DataPaymentInquiry.builder()
                                .transactionId(transaction.getTransactionId())
                                .status(transaction.getStatus())
                                .build();

                return ResponseEntity.status(HttpStatus.OK).body(ResponsePaymentInquiry.builder()
                                .success(true)
                                .message("Success")
                                .data(responsePaymentInquiry)
                                .build());
        }

        // Sale
        @PostMapping("/sale")
        public ResponseEntity<?> paymentSale(@RequestBody RequestPaymentSale body) {
                // check acquirerId is not null
                if (body.getAcquirerId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId is required")
                                        .build());
                }

                // check amount, cardNumber, requestId is not null
                if (body.getAcquirerId() == null || body.getAmount() == null ||
                                body.getCardNumber() == null || body.getRequestId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Amount, CardNumber, RequestId is required")
                                        .build());
                }

                // check acquirer is not null
                Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
                if (acquirer == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer not found")
                                        .build());
                }

                // check acquirer status is not active
                if (!acquirer.getStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer is not active")
                                        .build());
                }

                // check card is not null
                Card card = cardService.findCardByNumber(body.getCardNumber());
                if (card == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card not found")
                                        .build());
                }

                // check card status is not active
                if (!card.getCardStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card is not active")
                                        .build());
                }

                // TODO check limit credit
                // if (false) {
                // return
                // ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                // .success(false)
                // .message("Limit credit")
                // .build());
                // }

                // check duplicate requestId
                Transaction transactionsRepeat = transactionService
                                .findTransactionByRequestId(body.getRequestId());
                if (transactionsRepeat != null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Duplicate request")
                                        .build());
                }

                // check insufficient balance
                Long newBalance = card.getBalance() - body.getAmount();
                if (newBalance < 0) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Insufficient balance")
                                        .build());
                }

                // create new transaction
                Transaction newTransaction = Transaction.builder()
                                .amount(body.getAmount())
                                .cardNumber(body.getCardNumber())
                                .acquirerId(body.getAcquirerId())
                                .requestId(body.getRequestId())
                                .status("sale")
                                .build();

                // save transaction
                Transaction transaction = transactionService.saveTransaction(newTransaction);

                // save card
                card.setBalance(newBalance);
                cardService.saveCard(card);

                // create response
                DataPaymentSale responsePaymentSale = DataPaymentSale.builder()
                                .transactionId(transaction.getTransactionId())
                                .createDate(transaction.getCreateDate())
                                .createTime(transaction.getCreateTime())
                                .build();

                return ResponseEntity.status(HttpStatus.CREATED).body(ResponsePaymentSale.builder()
                                .success(true)
                                .message("Success")
                                .data(responsePaymentSale)
                                .build());
        }

        // Void
        @PostMapping("/void")
        public ResponseEntity<?> paymentVoid(@RequestBody RequestPaymentVoid body) {
                Transaction transaction;

                // check acquirerId is not null
                if (body.getAcquirerId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId is required")
                                        .build());
                }

                // check transactionId or requestId is not null
                if (body.getTransactionId() == null || body.getRequestId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("TransactionId or RequestId is required")
                                        .build());
                }

                // check acquirer is not null
                Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
                if (acquirer == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer not found")
                                        .build());
                }

                // check acquirer status is not active
                if (!acquirer.getStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer is not active")
                                        .build());
                }

                // find transaction
                if (body.getRequestId() == null) {
                        transaction = transactionService.findTransactionByTransactionId(body.getTransactionId());
                } else if (body.getTransactionId() == null) {
                        transaction = transactionService.findTransactionByRequestId(body.getRequestId());
                } else {
                        transaction = transactionService.findTransactionByTransactionIdAndRequestId(
                                        body.getTransactionId(),
                                        body.getRequestId());
                }

                // check transaction is not null
                if (transaction == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Transaction not found")
                                        .build());
                }

                // check card is not null
                Card card = cardService.findCardByNumber(transaction.getCardNumber());
                if (card == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card not found")
                                        .build());
                }

                // check card status is not active
                if (!card.getCardStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card is not active")
                                        .build());
                }

                // check req and trans does match with acquirerId
                if (!transaction.getAcquirerId().equals(body.getAcquirerId())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId does not match")
                                        .build());
                }

                // check transaction status is sale
                if (!transaction.getStatus().equals("sale")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Only sale transaction can be void")
                                        .build());
                }

                // save transaction
                transaction.setStatus("void");
                transactionService.saveTransaction(transaction);

                // reverse balance
                card.setBalance(card.getBalance() + transaction.getAmount());
                cardService.saveCard(card);

                return ResponseEntity.status(HttpStatus.OK).body(ResponsePayment.builder()
                                .success(true)
                                .message("Success")
                                .build());
        }

        // Reverse
        @PostMapping("/reverse")
        public ResponseEntity<?> paymentReverse(@RequestBody RequestPaymentReverse body) {
                List<String> transactionStatusWork = List.of("sale", "void");

                // check acquirerId is not null
                if (body.getAcquirerId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId is required")
                                        .build());
                }

                // check requestId is not null
                if (body.getRequestId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("TransactionId or RequestId is required")
                                        .build());
                }

                // check acquirer is not null
                Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
                if (acquirer == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer not found")
                                        .build());
                }

                // check acquirer status is not active
                if (!acquirer.getStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer is not active")
                                        .build());
                }

                // check transaction is not null
                Transaction transaction = transactionService.findTransactionByRequestId(body.getRequestId());
                if (transaction == null) {
                        return ResponseEntity.status(HttpStatus.OK).body(ResponsePayment.builder()
                                        .success(true)
                                        .message("Transaction not found")
                                        .build());
                }

                // check card is not null
                Card card = cardService.findCardByNumber(transaction.getCardNumber());
                if (card == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card not found")
                                        .build());
                }

                // check card status is not active
                if (!card.getCardStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card is not active")
                                        .build());
                }

                // check req and trans does match with acquirerId
                if (!transaction.getAcquirerId().equals(body.getAcquirerId())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId does not match")
                                        .build());
                }

                // check status is sale or void
                if (!transactionStatusWork.contains(transaction.getStatus())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Only sale or void transaction can be reverse")
                                        .build());
                }

                // save transaction
                transaction.setStatus("reverse");
                transactionService.saveTransaction(transaction);

                // reverse transaction
                card.setBalance(card.getBalance() + transaction.getAmount());
                cardService.saveCard(card);

                return ResponseEntity.status(HttpStatus.OK).body(ResponsePayment.builder()
                                .success(true)
                                .message("Success")
                                .build());
        }

        // Settlement
        @PostMapping("/settlement")
        public ResponseEntity<?> paymentSettlement(@RequestBody RequestPaymentSettlement body) {
                // check acquirerId is not null
                if (body.getAcquirerId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId is required")
                                        .build());
                }

                // check totalAmount and totalTransaction is not null
                if (body.getTotalAmount() == null || body.getTotalTransaction() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId is required")
                                        .build());
                }

                // check acquirer is not null
                Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
                if (acquirer == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer not found")
                                        .build());
                }

                // check acquirer status is not active
                if (!acquirer.getStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer is not active")
                                        .build());
                }

                // check transaction is not null
                List<Transaction> transactions = transactionService
                                .findAllTransactionsByAcquirerIdAndStatus(body.getAcquirerId(), "sale");
                if (transactions.size() == 0) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Transaction not found")
                                        .build());
                }

                // check totalAmount and totalTransaction
                Double totalAmount = Double.valueOf(body.getTotalAmount());
                Double totalAmountSum = TransactionUtils.calculateTotalAmount(transactions);
                Integer totalTrans = Integer.valueOf(body.getTotalTransaction());
                Integer totalTransSum = Integer.valueOf(transactions.size());
                if (!totalAmountSum.equals(totalAmount)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("TotalAmount does not match")
                                        .build());
                } else if (!totalTransSum.equals(totalTrans)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("TotalTransaction does not match")
                                        .build());
                }

                // update transaction status
                TransactionUtils.updateAllStatus(transactions, "settlement");
                transactionService.saveAllTransactions(transactions);

                return ResponseEntity.status(HttpStatus.OK).body(ResponsePayment.builder()
                                .success(true)
                                .message("Success")
                                .build());
        }

        // Settlement
        @PostMapping("/batch-upload")
        public ResponseEntity<?> paymentBatchUpload(@RequestBody RequestPaymentBatchUpload body) {
                // check acquirerId is not null
                if (body.getAcquirerId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId is required")
                                        .build());
                }

                // check transactions is not null
                if (body.getTransactions() == null || body.getTransactions().size() == 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Transactions is required")
                                        .build());
                }

                // check acquirer is not null
                Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
                if (acquirer == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer not found")
                                        .build());
                }

                // check acquirer status is not active
                if (!acquirer.getStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer is not active")
                                        .build());
                }

                // find transaction
                List<Transaction> transactionsAllSale = transactionService
                                .findAllTransactionsByAcquirerIdAndStatus(body.getAcquirerId(), "sale");
                if (transactionsAllSale.size() == 0) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Transaction not found")
                                        .build());
                }

                // find transaction match with transactionId
                List<Transaction> transactionsAllMatch = TransactionUtils
                                .getMatchingTransactions(transactionsAllSale, body.getTransactions());

                // check each amount is match
                Boolean isAllMatch = TransactionUtils
                                .isTransactionMatchAmount(transactionsAllMatch, body.getTransactions());

                if (!isAllMatch) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Amount does not match")
                                        .build());
                }

                // update transaction status
                TransactionUtils.updateAllStatus(transactionsAllMatch, "settlement");
                transactionService.saveAllTransactions(transactionsAllMatch);

                return ResponseEntity.status(HttpStatus.OK).body(ResponsePayment.builder()
                                .success(true)
                                .message("Success")
                                .build());
        }

        // Refund
        @PostMapping("/refund")
        public ResponseEntity<?> paymentRefund(@RequestBody RequestPaymentRefund body) {
                Transaction transaction;

                // check acquirerId is not null
                if (body.getAcquirerId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId is required")
                                        .build());
                }

                // check transactionId or requestId is not null
                if (body.getTransactionId() == null || body.getRequestId() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("TransactionId or RequestId is required")
                                        .build());
                }

                // check acquirer is not null
                Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
                if (acquirer == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer not found")
                                        .build());
                }

                // check acquirer status is not active
                if (!acquirer.getStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Acquirer is not active")
                                        .build());
                }

                // find transaction
                if (body.getRequestId() == null) {
                        transaction = transactionService.findTransactionByTransactionId(body.getTransactionId());
                } else if (body.getTransactionId() == null) {
                        transaction = transactionService.findTransactionByRequestId(body.getRequestId());
                } else {
                        transaction = transactionService.findTransactionByTransactionIdAndRequestId(
                                        body.getTransactionId(),
                                        body.getRequestId());
                }

                // check transaction is not null
                if (transaction == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Transaction not found")
                                        .build());
                }

                // check card is not null
                Card card = cardService.findCardByNumber(transaction.getCardNumber());
                if (card == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card not found")
                                        .build());
                }

                // check card status is not active
                if (!card.getCardStatus().equals("active")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Card is not active")
                                        .build());
                }

                // check req and trans does match with acquirerId
                if (!transaction.getAcquirerId().equals(body.getAcquirerId())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("AcquirerId does not match")
                                        .build());
                }

                // check status is settlement
                if (!transaction.getStatus().equals("settlement")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponsePayment.builder()
                                        .success(false)
                                        .message("Only settlement transaction can be refund")
                                        .build());
                }

                // save transaction
                transaction.setStatus("refund");
                transactionService.saveTransaction(transaction);

                // reverse transaction
                card.setBalance(card.getBalance() + transaction.getAmount());
                cardService.saveCard(card);

                return ResponseEntity.status(HttpStatus.OK).body(ResponsePayment.builder()
                                .success(true)
                                .message("Success")
                                .build());
        }
}