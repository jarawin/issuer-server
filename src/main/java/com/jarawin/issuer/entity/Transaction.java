package com.jarawin.issuer.entity;

import lombok.Data;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jarawin.issuer.dto.composite.TransactionRequestId;

@Data
@Entity
@Table(name = "TRANSACTION")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransactionRequestId.class)
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class Transaction {

    @Id
    @JsonProperty("transactionId")
    private String transactionId;

    @Id
    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("createDate")
    private LocalDate createDate;

    @JsonProperty("createTime")
    private LocalTime createTime;

    @JsonProperty("cardNumber")
    private Long cardNumber;

    @JsonProperty("acquirerId")
    private String acquirerId;

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("status")
    private String status;
}