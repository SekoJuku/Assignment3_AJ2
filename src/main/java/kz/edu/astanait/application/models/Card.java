package kz.edu.astanait.application.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "card_number", unique = true)
    private String cardNumber;

    @Column(name = "expire_date")
    private String expireDate;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "kzt", precision = 18, scale = 2)
    private Double kzt;

    @Column(name = "rub", precision = 18, scale = 2)
    private Double rub;

    @Column(name = "usd", precision = 18, scale = 2)
    private Double usd;

    @Column(name = "user_id")
    private Long userId;

    public Card() {
        kzt = 0D;
        rub = 0D;
        usd = 0D;
    }

    public static Double round(double value, int scale)
            throws IllegalArgumentException {
        if(scale < 0) throw new IllegalArgumentException();
        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        bigDecimal = bigDecimal.setScale(scale, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setKzt(Double kzt) {
        kzt -= kzt * 0.01;
        setKzt(kzt,false);
    }

    public void setKzt(Double kzt, boolean a) {
        this.kzt = Card.round(kzt,2);
    }

    public void setRub(Double rub) {
        rub -= rub *0.01;
        setRub(rub,false);
    }

    public void setRub(Double rub, boolean a) {
        this.rub = Card.round(rub, 2);
    }

    public void setUsd(Double usd) {
        usd -= usd * 0.01;
        setUsd(usd,false);
    }

    public void setUsd(Double usd, boolean a) {
        this.usd = Card.round(usd, 2);
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
