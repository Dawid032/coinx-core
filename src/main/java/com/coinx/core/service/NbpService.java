package com.coinx.core.service;

import com.coinx.core.model.ExchangeRate;
import com.coinx.core.model.dto.NbpResponse;
import com.coinx.core.repository.ExchangeRateRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class NbpService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public NbpService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void fetchRates() {
        System.out.println("--- GETTING CURRENCY PRICE UPDATE FROM NBP ---");
        String url = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

        NbpResponse[] response = restTemplate.getForObject(url, NbpResponse[].class);
        if(response != null && response.length > 0) {
            NbpResponse tableA = response[0];

            for(NbpResponse.NbpRate rate : tableA.getRates()) {
                if(rate.getCode().equals("USD") || rate.getCode().equals("EUR")) {
                    saveOrUpdateRate(rate.getCode(), rate.getMid());
                }
            }
        }
        System.out.println("--- PRICE HAS UPDATE ---");
    }
    private void saveOrUpdateRate(String currencyCode, java.math.BigDecimal rateValue) {

        Optional<ExchangeRate> existingRate = exchangeRateRepository.findByCurrency(currencyCode);
        if(existingRate.isPresent()) {
            ExchangeRate rate = existingRate.get();
            rate.setRate(rateValue);
            exchangeRateRepository.save(rate);
        } else {
            ExchangeRate newRate = new ExchangeRate(currencyCode, rateValue);
            exchangeRateRepository.save(newRate);
        }
    }
}
