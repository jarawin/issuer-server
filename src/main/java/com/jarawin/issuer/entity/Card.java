package com.jarawin.issuer.entity;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CARD")
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class Card {

    // card id
    @Id
    @JsonProperty("cardNumber")
    private Long cardNumber;

    @JsonProperty("cvv")
    private Long cvv;

    @JsonProperty("acquirerId")
    private String acquirerId;

    // card info
    @JsonProperty("cardType")
    private String cardType;

    @JsonProperty("cardHolderName")
    private String cardHolderName;

    @JsonProperty("cardStatus")
    private String cardStatus;

    // dates
    @JsonProperty("createDate")
    private LocalDate createDate;

    @JsonProperty("expirationDate")
    private LocalDate expirationDate;

    // balance
    @JsonProperty("balance")
    private Long balance;

    @JsonProperty("creditLimit")
    private Long creditLimit;

    @JsonProperty("overdue")
    private Long overdue;
}
