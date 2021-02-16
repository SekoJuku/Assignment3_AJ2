package kz.edu.astanait.application.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeMoneyDTO {
    private String cardNumber;
    private String currencyType;
    private Double amount;
}
