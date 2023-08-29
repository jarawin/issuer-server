package com.jarawin.issuer.dto.payment.response.data;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class DataPaymentSale {
    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("createDate")
    private LocalDate createDate;

    @JsonProperty("createTime")
    private LocalTime createTime;
}
