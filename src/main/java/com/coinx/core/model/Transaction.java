package com.coinx.core.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String fromCurrency;

    @Column(nullable = false)
    private String toCurrency;

    @Column(nullable = false)
    private BigDecimal amountSold;

    @Column(nullable = false)
    private BigDecimal amountBought;

    @Column(nullable = false)
    private BigDecimal rateUsed;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Transaction(UUID userId, String fromCurrency,  String toCurrency, BigDecimal amountSold, BigDecimal amountBought, BigDecimal rateUsed) {
        this.userId = userId;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amountSold = amountSold;
        this.amountBought = amountBought;
        this.rateUsed = rateUsed;
        this.timestamp = LocalDateTime.now();
    }
}
