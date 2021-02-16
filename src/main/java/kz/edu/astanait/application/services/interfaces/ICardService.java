package kz.edu.astanait.application.services.interfaces;

import kz.edu.astanait.application.exceptions.ConvertException;
import kz.edu.astanait.application.exceptions.IncomeException;
import kz.edu.astanait.application.exceptions.TransferException;
import kz.edu.astanait.application.models.*;

import java.util.List;
import java.util.Set;

public interface ICardService {
    void transferMoney(MoneyTransferDTO moneyTransferDTO, User fromUser) throws TransferException;
    Card findByCardNumber(String cardNumber);
    Set<Card> findByUserId(Long id);
    void createCard(User user);
    void convertMoney(ConvertMoneyDTO convertMoneyDTO) throws ConvertException;
    List<Card> getAll();
    void makeIncome(IncomeMoneyDTO incomeMoneyDTO, User userCard) throws IncomeException;
}
