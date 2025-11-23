package com.coinx.core.controller;


import com.coinx.core.model.ExchangeRate;
import com.coinx.core.repository.ExchangeRateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/{rates}")
public class ExchangeRateController {
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateController(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @PostMapping
    public ResponseEntity<ExchangeRate> createOrUpdateRate(@RequestParam String code, @RequestParam BigDecimal rate) {

        Optional<ExchangeRate> existingRate = exchangeRateRepository.findByCurrency(code);

        if (existingRate.isPresent()) {
            ExchangeRate rateToUpdate = existingRate.get();
            rateToUpdate.setRate(rate);
            return ResponseEntity.ok(exchangeRateRepository.save(rateToUpdate));
        } else {
            ExchangeRate newRate = new ExchangeRate(code, rate);
            return ResponseEntity.ok(exchangeRateRepository.save(newRate));
        }
    }

        @GetMapping("/{code}")
                public ResponseEntity<ExchangeRate> getRate(@PathVariable String code){
                return exchangeRateRepository.findByCurrency(code)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        }
    }
