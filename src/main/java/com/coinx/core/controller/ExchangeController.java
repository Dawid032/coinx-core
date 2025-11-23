package com.coinx.core.controller;

import com.coinx.core.model.dto.ExchangeRequest;
import com.coinx.core.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {
    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }
@PostMapping
    public ResponseEntity<String> performExchange(@RequestBody ExchangeRequest request) {
        try {
            exchangeService.exchangeCurrency(request);

            return ResponseEntity.ok("Exchange complete!");
        } catch (RuntimeException e)
            {
            return ResponseEntity.badRequest().body(e.getMessage());
            }
    }
}
