package net.guides.springboot2.springboot2webappjsp.domain;

import javax.persistence.*;
import java.time.Instant;

@Table(name = "payments")
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payments_serial_id", nullable = false)
    private Integer id;

    @Column(name = "pay_time", nullable = false)
    private Instant payTime;

    @Column(name = "card_number", nullable = false, length = 45)
    private String cardNumber;

    @Column(name = "cvv", nullable = false, length = 3)
    private String cvv;

    @Column(name = "valid_time", nullable = false)
    private Instant validTime;

    @Column(name = "holder_name", nullable = false, length = 45)
    private String holderName;

    public Payment() {

    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Instant getValidTime() {
        return validTime;
    }

    public void setValidTime(Instant validTime) {
        this.validTime = validTime;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Instant getPayTime() {
        return payTime;
    }

    public void setPayTime(Instant payTime) {
        this.payTime = payTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}