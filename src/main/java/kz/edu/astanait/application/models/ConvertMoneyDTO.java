package kz.edu.astanait.application.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConvertMoneyDTO {
    private String cardNumber;
    private String fromCurrency;
    private String toCurrency;
    private Double moneyAmount;
}
