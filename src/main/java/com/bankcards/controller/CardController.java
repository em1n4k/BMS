package com.bankcards.controller;

import com.bankcards.dto.CardCreationRequest;
import com.bankcards.dto.CardResponse;
import com.bankcards.dto.CardTransferRequest;
import com.bankcards.dto.TransferResponse;
import com.bankcards.service.CardService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponse> getCardById(@PathVariable Long id) {
        CardResponse response = cardService.getCardById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> getAllCards() {
        List<CardResponse> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody CardTransferRequest request) {
        TransferResponse response = cardService.transferBetweenOwnCards(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blockCard(@PathVariable Long id) {
        cardService.blockCard(id);
        return ResponseEntity.ok("Card blocked successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CardResponse> createCard(@RequestBody CardCreationRequest request) {
       CardResponse response = cardService.createCardForUser(
               request.getUserId(),
               request.getCurrency()
       );

       return ResponseEntity.ok(response);
    }
}
