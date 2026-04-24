package com.bankcards.service;

import com.bankcards.dto.CardResponse;
import com.bankcards.dto.CardTransferRequest;
import com.bankcards.dto.TransferResponse;
import com.bankcards.entity.Card;
import com.bankcards.entity.enums.CardStatus;
import com.bankcards.entity.enums.Currency;
import com.bankcards.exception.CardAccessDeniedException;
import com.bankcards.exception.CardNotFoundException;
import com.bankcards.exception.InvalidTransferException;
import com.bankcards.repository.CardRepository;
import com.bankcards.repository.UserRepository;
import com.bankcards.util.CardUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    // Getting all cards for a user
    @Transactional(readOnly = true)
    public List<Card> getCardsForUser(Long userId) {
        return cardRepository.findByOwnerId(userId)
                .stream()
                .sorted(Comparator.comparing(Card::getCardNumber))
                .toList();
    }

    // Creating a new bank card for the user
    // Currency defaults to RUB, but can be changed
    @Transactional
    public CardResponse createCardForUser(Long userId, String currency) {

        var owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Card card = new Card();
        card.setCardNumber(generateNewNumber());
        card.setCurrency(CardUtil.parseCurrencyOrDefault(currency));
        card.setBalance(BigDecimal.ZERO);
        card.setStatus(CardStatus.ACTIVE);
        card.setExpirationDate(LocalDate.now().plusYears(3));


        owner.addCard(card);

        Card savedCard = cardRepository.save(card);

        return mapToCardResponse(savedCard);
    }

    // Block bank card by id
    @Transactional
    public void blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found: " + cardId));

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    // Find card by ID
    // with additional role access checking
    @Transactional(readOnly = true)
    public CardResponse getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found: " + cardId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUsername = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority
                        .getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !card.getOwner().getUsername().equals(currentUsername)) {
            throw new CardAccessDeniedException("Access denied");
        }

        return new CardResponse(
                card.getId(),
                card.getCurrency(),
                CardUtil.maskCardNumber(card.getCardNumber()),
                card.getBalance(),
                card.getStatus(),
                card.getCreatedAt(),
                card.getExpirationDate()
        );
    }

    // ADMIN gets all cards
    // USER gets only their own cards
    @Transactional(readOnly = true)
    public List<CardResponse> getAllCards() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(role -> role.getAuthority()
                        .equals("ROLE_ADMIN"));

        List<Card> cards;

        if(isAdmin) {
            cards = cardRepository.findAll();
        } else {
            cards = cardRepository.findByOwnerUsername(username);
        }

        return cards.stream()
                .map(this::mapToCardResponse)
                .toList();
    }

    // Issue replacement card (lost/compromised)
    @Transactional
    public Card issueReplacement(Long oldCardId) {

        Card old = cardRepository.findById(oldCardId)
                .orElseThrow(() -> new RuntimeException("Card not found: " + oldCardId));

        old.setStatus(CardStatus.BLOCKED);

        Card replacementCard = new Card();
        replacementCard.setCardNumber(generateNewNumber());
        replacementCard.setCurrency(old.getCurrency());
        replacementCard.setBalance(old.getBalance()); // copy balance
        replacementCard.setStatus(CardStatus.ACTIVE);

        old.getOwner().addCard(replacementCard);

        return cardRepository.save(replacementCard);
    }

                        /*  helpers  */

    private CardResponse mapToCardResponse(Card card) {
        return new CardResponse(
                card.getId(),
                card.getCurrency(),
                CardUtil.maskCardNumber(card.getCardNumber()),
                card.getBalance(),
                card.getStatus(),
                card.getCreatedAt(),
                card.getExpirationDate()
        );
    }

    @Transactional
    public TransferResponse transferBetweenOwnCards(CardTransferRequest cardRequest) {
            Card fromCard = cardRepository.findById(cardRequest.getFromCardId())
                    .orElseThrow(() -> new RuntimeException("Source card not found"));

            Card toCard = cardRepository.findById(cardRequest.getToCardId())
                    .orElseThrow(() -> new RuntimeException("Target card not found"));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String username = authentication.getName();

            if (!fromCard.getOwner().getUsername().equals(username) ||
               !toCard.getOwner().getUsername().equals(username)) {
                throw new InvalidTransferException("You can only transfer between your own cards!");
            }

            if (fromCard.getId().equals(toCard.getId())) {
                throw new InvalidTransferException("Can't transfer to the same card");
            }

            if (fromCard.getBalance().compareTo(cardRequest.getAmount()) < 0) {
                throw new InvalidTransferException("Insufficient balance");
            }

            fromCard.setBalance(fromCard.getBalance().subtract(cardRequest.getAmount()));
            toCard.setBalance(toCard.getBalance().add(cardRequest.getAmount()));

            cardRepository.save(fromCard);
            cardRepository.save(toCard);

            return new TransferResponse(
                    fromCard.getId(),
                    toCard.getId(),
                    cardRequest.getAmount(),
                    fromCard.getBalance(),
                    toCard.getBalance(),
                    "Transfer successful!"
            );
    }

    // Unique card number generation
    private String generateNewNumber() {
        String num;
        do {
            num = CardUtil.randomDigits(16);
        } while (cardRepository.existsByCardNumber(num));
        return num;
    }
}