package kz.edu.astanait.application.repositories;

import kz.edu.astanait.application.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    Card findByCardNumber(String cardNumber);
    Set<Card> findByUserId(Long id);
    boolean existsCardByCardNumber(String cardNumber);
    @Modifying
    @Query("update Card c set c.kzt = ?1 where c.id = ?2")
    void setKZTById(Double amount, Long userId);
    @Modifying
    @Query("update Card c set c.rub = ?1 where c.id = ?2")
    void setRUBById(Double amount, Long userId);
    @Modifying
    @Query("update Card c set c.usd = ?1 where c.id = ?2")
    void setUSDById(Double amount, Long userId);
}
