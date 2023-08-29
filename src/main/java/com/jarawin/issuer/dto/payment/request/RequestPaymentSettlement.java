package com.jarawin.issuer.dto.payment.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class RequestPaymentSettlement {
    @JsonProperty("acquirerId")
    private String acquirerId;

    @JsonProperty("totalAmount")
    private String totalAmount;

    @JsonProperty("totalTransaction")
    private String totalTransaction;
}
