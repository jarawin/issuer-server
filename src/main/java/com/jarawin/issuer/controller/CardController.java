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
import org.springframework.web.bind.annotation.RestController;

import com.jarawin.issuer.entity.Acquirer;
import com.jarawin.issuer.entity.Card;
import com.jarawin.issuer.service.AcquirerService;
import com.jarawin.issuer.service.CardService;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    CardService cardService;

    @Autowired
    AcquirerService acquirerService;

    // Read all
    @GetMapping()
    public List<Card> getCards() {
        return cardService.findAllCards();
    }

    // Read by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCard(@PathVariable Long id) {
        Card card = cardService.findCardByNumber(id);
        if (card == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card);
    }

    // Create
    @PostMapping()
    public ResponseEntity<?> postCard(@Valid @RequestBody Card body) {
        Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
        if (acquirer == null) {
            return ResponseEntity.notFound().build();
        }

        Card card = cardService.saveCard(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<?> putCard(@PathVariable Long id, @Valid @RequestBody Card body) {
        Card card = cardService.findCardByNumber(id);
        if (card == null) {
            return ResponseEntity.notFound().build();
        }

        Acquirer acquirer = acquirerService.findAcquirerById(body.getAcquirerId());
        if (acquirer == null) {
            return ResponseEntity.notFound().build();
        }

        card = cardService.updateCard(card, body);
        return ResponseEntity.ok(card);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id) {
        Card card = cardService.findCardByNumber(id);
        if (card == null) {
            return ResponseEntity.notFound().build();
        }
        cardService.deleteCard(card);
        return ResponseEntity.ok().build();
    }
}