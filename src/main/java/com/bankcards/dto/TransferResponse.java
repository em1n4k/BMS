package com.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferResponse {

    @NotNull
    private Long fromCardId;

    @NotNull
    private Long toCardId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Positive
    private BigDecimal fromCardBalance;

    @NotNull
    private BigDecimal toCardBalance;

    @NotNull
    private String message;

    public TransferResponse () {
        // no-args constructor
    }

    public TransferResponse(Long fromCardId, Long toCardId,
                            BigDecimal amount, BigDecimal fromCardBalance,
                            BigDecimal toCardBalance, String message) {

        this.fromCardId = fromCardId;
        this.toCardId = toCardId;
        this.amount = amount;
        this.fromCardBalance = fromCardBalance;
        this.toCardBalance = toCardBalance;
        this.message = message;
    }

    /* getters & setters */

    public Long getFromCardId () {
        return fromCardId;
    }

    public void setFromCardId (Long fromCardId) {
        this.fromCardId = fromCardId;
    }

    public Long getToCardId () {
        return toCardId;
    }

    public void setToCardId (Long toCardId) {
        this.toCardId = toCardId;
    }

    public BigDecimal getAmount () {
        return amount;
    }

    public void setAmount (BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFromCardBalance () {
        return fromCardBalance;
    }

    public void setFromCardBalance (BigDecimal fromCardBalance) {
        this.fromCardBalance = fromCardBalance;
    }

    public BigDecimal getToCardBalance () {
        return toCardBalance;
    }

    public void setToCardBalance (BigDecimal toCardBalance) {
        this.toCardBalance = toCardBalance;
    }

    public String getMessage () {
        return message;
    }

    public void setMessage (String message) {
        this.message = message;
    }
}
