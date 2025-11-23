package com.coinx.core.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NbpResponse {
    private String table;
    private String no;
    private String effectiveDate;
    private List<NbpRate> rates;

    @Data
    public static class NbpRate {
        private String currency;
        private String code;
        private BigDecimal mid;
    }
}
