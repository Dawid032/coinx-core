package com.coinx.core.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "exchange_rates")
@Data
@NoArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String currency;

    @Column(nullable = false)
    private BigDecimal rate;

    public ExchangeRate(String currency, BigDecimal rate) {
        this.currency = currency;
        this.rate = rate;
    }


}
