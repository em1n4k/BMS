package com.bankcards.repository;

import com.bankcards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByOwnerId(Long ownerId);

    boolean existsByCardNumber(String cardNumber);

    List<Card> findByOwnerUsername(String username);
}
