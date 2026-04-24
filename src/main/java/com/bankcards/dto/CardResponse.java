package com.bankcards.dto;

import com.bankcards.entity.enums.CardStatus;
import com.bankcards.entity.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CardResponse {

    private Long id;
    private Currency currency;
    private String cardNumber;
    private BigDecimal balance;
    private CardStatus status;
    private LocalDateTime createdAt;
    private LocalDate expirationDate;

    public CardResponse() {
    }

    public CardResponse(Long id, Currency currency, String cardNumber,
                        BigDecimal balance, CardStatus status,
                        LocalDateTime createdAt,
                        LocalDate expirationDate) {

        this.id = id;
        this.currency = currency;
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.expirationDate = expirationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
