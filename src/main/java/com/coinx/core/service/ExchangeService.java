package com.coinx.core.service;

import com.coinx.core.model.ExchangeRate;
import com.coinx.core.model.Transaction;
import com.coinx.core.model.Wallet;
import com.coinx.core.model.dto.ExchangeRequest;
import com.coinx.core.repository.ExchangeRateRepository;
import com.coinx.core.repository.TransactionRepository;
import com.coinx.core.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeService {
    private WalletRepository walletRepository;
    private ExchangeRateRepository exchangeRateRepository;
    private final TransactionRepository transactionRepository;

    public ExchangeService(WalletRepository walletRepository, ExchangeRateRepository exchangeRateRepository,
                           TransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.transactionRepository = transactionRepository;
    }
@Transactional
    public void exchangeCurrency(ExchangeRequest request) {

        // We're looking for a source wallet, which one we will get money
        Wallet sourceWallet = walletRepository.findByUserIdAndCurrency(request.getUserId(), request.getFromCurrency())
                .orElseThrow(() -> new RuntimeException("Source wallet not found"));

        // We're looking for a wallet to add money to
        Wallet targetWallet = walletRepository.findByUserIdAndCurrency(request.getUserId(), request.getToCurrency())
                .orElseThrow(() -> new RuntimeException("Target wallet not found"));

        // We obtain the exchange rate for the currency we want to buy
        ExchangeRate rate = exchangeRateRepository.findByCurrency(request.getToCurrency())
                .orElseThrow(() -> new RuntimeException("Exchange rate not found" + request.getToCurrency()));

        // Math calculating
        BigDecimal amountToBuy = request.getAmount();
        BigDecimal exchangeRate = rate.getRate();
        BigDecimal cost = amountToBuy.multiply(exchangeRate);

        // Balance validation
        if(sourceWallet.getBalance().compareTo(cost) < 0) {
            throw new RuntimeException("Insufficient funds in your account! You need: " +  cost);
        }
        BigDecimal newSourceBalance = sourceWallet.getBalance().subtract(cost);
        sourceWallet.setBalance(newSourceBalance);

        // adding new bought amount to wallet
        BigDecimal newTargetBalance = targetWallet.getBalance().add(amountToBuy);
        targetWallet.setBalance(newTargetBalance);

        // save to database
        walletRepository.save(sourceWallet);
        walletRepository.save(targetWallet);

        // Adding logs of transactions
        Transaction transaction = new Transaction(
                request.getUserId(),
                request.getFromCurrency(),
                request.getToCurrency(),
                cost,
                amountToBuy,
                exchangeRate
        );
        transactionRepository.save(transaction);
    }
}
