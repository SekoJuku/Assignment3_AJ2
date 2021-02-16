package kz.edu.astanait.application.models;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferDTO {
    private String fromCardNumber;
    private String targetCardNumber;
    private String currencyType;
    private Double moneyAmount;
}
