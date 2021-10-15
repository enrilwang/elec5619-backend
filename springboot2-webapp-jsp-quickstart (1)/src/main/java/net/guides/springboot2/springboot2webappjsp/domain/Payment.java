package net.guides.springboot2.springboot2webappjsp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.Instant;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
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



}