package kz.edu.astanait.application.services;

import kz.edu.astanait.application.exceptions.ConvertException;
import kz.edu.astanait.application.exceptions.IncomeException;
import kz.edu.astanait.application.exceptions.TransferException;
import kz.edu.astanait.application.models.*;
import kz.edu.astanait.application.repositories.CardRepository;
import kz.edu.astanait.application.services.interfaces.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class CardService implements ICardService {
    private final CardRepository cardRepository;

    private final String KZT = "KZT";
    private final String USD = "USD";
    private final String RUB = "RUB";

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }



    @Override
    public Card findByCardNumber(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    @Override
    public Set<Card> findByUserId(Long id) {
        return cardRepository.findByUserId(id);
    }

    @Override
    public void createCard(User user) {
        Card card = new Card();
        Random random = new Random();

        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while(cardRepository.existsCardByCardNumber(cardNumber));
        card.setCardNumber(cardNumber);
        card.setCvv(String.valueOf((random.nextInt(1000))));

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        currentDate.add(Calendar.YEAR, 3);
        card.setExpireDate(dateFormat.format(currentDate.getTime()));
        card.setUserId(user.getId());

        cardRepository.save(card);
    }

    private String generateCardNumber() {
        Random random = new Random();
        String cardNumber = "5522";
        for (int i = 0; i < 12; i++) {
            int digit = random.nextInt(10);
            cardNumber += Integer.toString(digit);
        }
        return cardNumber;
    }

    @Override
    public void convertMoney(ConvertMoneyDTO convertMoneyDTO) throws ConvertException {
        Card card = cardRepository.findByCardNumber(convertMoneyDTO.getCardNumber());
        if (card == null) {
            throw new ConvertException("This is not your card!");
        }

        String currencyFrom = convertMoneyDTO.getFromCurrency();
        double moneyFrom, moneyToConvert = convertMoneyDTO.getMoneyAmount();
        switch (currencyFrom) {
            case KZT:
                moneyFrom = card.getKzt();
                if (moneyFrom - moneyToConvert < 0) {
                    throw new ConvertException("Not enough tenge!");
                }
                card.setKzt(card.getKzt() - moneyToConvert, false);
                break;

            case USD:
                moneyFrom = card.getUsd();
                if (moneyFrom - moneyToConvert < 0) {
                    throw new ConvertException("Not enough dollars!");
                }
                card.setUsd(card.getUsd() - moneyToConvert, false);
                break;

            case RUB:
                moneyFrom = card.getRub();
                if (moneyFrom - moneyToConvert < 0) {
                    throw new ConvertException("Not enough rubles!");
                }
                card.setRub(card.getRub() - moneyToConvert, false);
                break;

            default:
                throw new ConvertException("Wrong currency!");
        }
        String currencyTo = convertMoneyDTO.getToCurrency();

        double convertedMoney = moneyToConvert * convertFromTo(currencyFrom, currencyTo);
        switch (currencyTo) {
            case KZT:
                card.setKzt(card.getKzt() + convertedMoney, false);
                break;

            case USD:
                card.setUsd(card.getUsd() + convertedMoney, false);
                break;

            default:
                card.setRub(card.getRub() + convertedMoney, false);
        }

        cardRepository.save(card);
    }

    @Override
    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    private double convertFromTo(String currencyFrom, String currencyTo) {
        switch (currencyFrom) {
            case KZT:
                if (currencyTo.equals(USD)) return 0.0024;
                return 0.18;

            case USD:
                if (currencyTo.equals(KZT)) return 420.50;
                return 74.65;

            default://RUB
                if (currencyTo.equals(KZT)) return 5.63;
                return 0.013;
        }
    }

    @Override
    public void transferMoney(MoneyTransferDTO moneyTransferDTO, User fromUser)
            throws TransferException {
        Card fromCard = findByCardNumber(moneyTransferDTO.getFromCardNumber());

        if (!fromCard.getUserId().equals(fromCard.getId())) {
            throw new TransferException(fromCard.getCardNumber() + "Try again.");
        }
        if (moneyTransferDTO.getTargetCardNumber().startsWith("5522")) {
            if (!cardRepository.existsCardByCardNumber(moneyTransferDTO.getTargetCardNumber())) {
                throw new TransferException("There is no card with number " + moneyTransferDTO.getTargetCardNumber());
            }
            Card targetCard = findByCardNumber(moneyTransferDTO.getTargetCardNumber());
            double cut = 0;
            if (!targetCard.getUserId().equals(fromUser.getId()) && moneyTransferDTO.getMoneyAmount() > 100000)
                cut = 0.01;

            moneyTransferDTO.setMoneyAmount(moneyTransferDTO.getMoneyAmount() - moneyTransferDTO.getMoneyAmount() * cut);

            withdrawFromUserCard(moneyTransferDTO, fromCard); //get from user card

            //give to another card
            switch (moneyTransferDTO.getCurrencyType()) {
                case KZT:
                    targetCard.setKzt(moneyTransferDTO.getMoneyAmount());
                    break;

                case USD:
                    targetCard.setUsd(moneyTransferDTO.getMoneyAmount());
                    break;

                default://RUB
                    targetCard.setRub(moneyTransferDTO.getMoneyAmount());
                    break;
            }
            cardRepository.save(targetCard);

        } else {
            moneyTransferDTO.setMoneyAmount(
                    Card.round(moneyTransferDTO.getMoneyAmount() - moneyTransferDTO.getMoneyAmount() * 0.01,2)
            );
            withdrawFromUserCard(moneyTransferDTO, fromCard);
        }
    }

    private void withdrawFromUserCard(MoneyTransferDTO moneyTransferDTO, Card userCard)
            throws TransferException {
        double moneyToTransfer = moneyTransferDTO.getMoneyAmount();
        double moneyFrom;
        switch (moneyTransferDTO.getCurrencyType()) {
            case KZT:
                moneyFrom = userCard.getKzt();
                if (moneyFrom - moneyToTransfer < 0) {
                    throw new TransferException("Not enough tenge!");
                }
                userCard.setKzt(moneyFrom - moneyToTransfer);
                break;

            case USD:
                moneyFrom = userCard.getUsd();
                if (moneyFrom - moneyToTransfer < 0) {
                    throw new TransferException("Not enough dollars!");
                }
                userCard.setUsd(moneyFrom - moneyToTransfer);
                break;

            case RUB:
                moneyFrom = userCard.getRub();
                if (moneyFrom - moneyToTransfer < 0) {
                    throw new TransferException("Not enough rubles!");
                }
                userCard.setRub(moneyFrom - moneyToTransfer);
                break;

            default:
                throw new TransferException("Wrong currency!");
        }
        cardRepository.save(userCard);
    }

    public void makeIncome(IncomeMoneyDTO incomeMoneyDTO, User user)
                throws IncomeException {
        Card card = findByCardNumber(incomeMoneyDTO.getCardNumber());
        boolean u = false;
        for(Card c : user.getBankCards()) {
            if(c.getCardNumber().equals(card.getCardNumber())) {
                u = true;
            }
        }
        if(!u) {
            throw new IncomeException(card.getCardNumber() + "Try again.");
        }
        if (incomeMoneyDTO.getCardNumber().startsWith("5522")) {
            if (!cardRepository.existsCardByCardNumber(incomeMoneyDTO.getCardNumber())) {
                throw new IncomeException("No card with number " + incomeMoneyDTO.getCardNumber());
            }
            getIncome(incomeMoneyDTO,card);
        }
    }

    public void getIncome(IncomeMoneyDTO incomeMoneyDTO, Card userCard)
            throws IncomeException {
        double amount = incomeMoneyDTO.getAmount();
        double prevsum;
        switch (incomeMoneyDTO.getCurrencyType()) {
            case KZT:
                prevsum = userCard.getKzt();
                userCard.setKzt(prevsum+amount,false);
                break;
            case RUB:
                prevsum = userCard.getRub();
                userCard.setRub(prevsum+amount,false);
                break;
            case USD:
                prevsum = userCard.getUsd();
                userCard.setUsd(prevsum+amount,false);
                break;
            default:
                throw new IncomeException("Wrong currency!");
        }
        cardRepository.save(userCard);
    }
}
