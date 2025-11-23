package com.coinx.core.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ExchangeRequest {
    private UUID userId;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal amount;
}
