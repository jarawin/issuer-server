package com.jarawin.issuer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jarawin.issuer.entity.Card;
import com.jarawin.issuer.repository.CardRepository;
import com.jarawin.issuer.util.CardNumberSimulator;
import java.time.LocalDate;

@Service
public class CardService {
    @Autowired
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card saveCard(Card card) {
        if (card.getCardNumber() == null) {
            Long cardNumber = CardNumberSimulator.generateRandomCardNumber();
            card.setCardNumber(cardNumber);

            Long cvv = CardNumberSimulator.simulateCvv();
            card.setCvv(cvv);

            card.setCreateDate(LocalDate.now());
            card.setExpirationDate(LocalDate.now().plusYears(4));

            card.setCardStatus("pending");
            card.setOverdue((long) 0);
        }
        return cardRepository.save(card);
    }

    public Card updateCard(Card oldData, Card newData) {
        if (newData.getCardNumber() != null) {
            oldData.setCardNumber(newData.getCardNumber());
        }
        if (newData.getCardStatus() != null) {
            oldData.setCardStatus(newData.getCardStatus());
        }
        if (newData.getCvv() != null) {
            oldData.setCvv(newData.getCvv());
        }
        if (newData.getCreateDate() != null) {
            oldData.setCreateDate(newData.getCreateDate());
        }
        if (newData.getExpirationDate() != null) {
            oldData.setExpirationDate(newData.getExpirationDate());
        }
        if (newData.getAcquirerId() != null) {
            oldData.setAcquirerId(newData.getAcquirerId());
        }
        if (newData.getBalance() != null) {
            oldData.setBalance(newData.getBalance());
        }
        if (newData.getOverdue() != null) {
            oldData.setOverdue(newData.getOverdue());
        }

        return cardRepository.save(oldData);
    }

    public List<Card> findAllCards() {
        return (List<Card>) cardRepository.findAll();
    }

    public Card findCardByNumber(Long number) {
        return cardRepository.findById(number).orElse(null);
    }

    public void deleteCard(Card Card) {
        cardRepository.delete(Card);
    }
}