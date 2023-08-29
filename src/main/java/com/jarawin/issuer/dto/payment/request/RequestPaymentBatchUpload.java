package com.jarawin.issuer.dto.payment.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.jarawin.issuer.entity.Transaction;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class RequestPaymentBatchUpload {
    @JsonProperty("acquirerId")
    private String acquirerId;

    @JsonProperty("transactions")
    private List<Transaction> transactions;
}
