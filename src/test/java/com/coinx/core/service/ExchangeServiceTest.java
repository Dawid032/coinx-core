package com.coinx.core.service;

import com.coinx.core.model.ExchangeRate;
import com.coinx.core.model.dto.ExchangeRequest;
import com.coinx.core.model.Wallet;
import com.coinx.core.repository.ExchangeRateRepository;
import com.coinx.core.repository.TransactionRepository;
import com.coinx.core.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 1. Włączamy Mockito
class ExchangeServiceTest {

    @Mock // 2. To są fałszywe zależności (atrapy)
    private WalletRepository walletRepository;
    @Mock
    private ExchangeRateRepository exchangeRateRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks // 3. Tu wstrzykujemy atrapy do prawdziwego serwisu
    private ExchangeService exchangeService;

    @Test
    void shouldExchangeCurrencySuccessfully() {
        // --- GIVEN (Przygotowanie danych) ---
        UUID userId = UUID.randomUUID();

        // Przygotowujemy "udawane" portfele
        Wallet plnWallet = new Wallet();
        plnWallet.setBalance(new BigDecimal("1000.00"));

        Wallet usdWallet = new Wallet();
        usdWallet.setBalance(new BigDecimal("0.00"));

        // Przygotowujemy "udawany" kurs
        ExchangeRate rate = new ExchangeRate("USD", new BigDecimal("4.00"));

        // Uczymy atrapy, co mają zwracać (tzw. Stubbing)
        when(walletRepository.findByUserIdAndCurrency(userId, "PLN")).thenReturn(Optional.of(plnWallet));
        when(walletRepository.findByUserIdAndCurrency(userId, "USD")).thenReturn(Optional.of(usdWallet));
        when(exchangeRateRepository.findByCurrency("USD")).thenReturn(Optional.of(rate));

        // Tworzymy request (chcemy kupić 100 USD)
        ExchangeRequest request = new ExchangeRequest();
        request.setUserId(userId);
        request.setFromCurrency("PLN");
        request.setToCurrency("USD");
        request.setAmount(new BigDecimal("100.00"));

        // --- WHEN ---
        exchangeService.exchangeCurrency(request);

        assertEquals(0, new BigDecimal("600.00").compareTo(plnWallet.getBalance()), "Saldo PLN się nie zgadza");
        // 0 + 100 = 100
        assertEquals(0, new BigDecimal("100.00").compareTo( usdWallet.getBalance()), "Saldo USD sie nie zgadza");

        // 2. Weryfikujemy, czy serwis kazał zapisać zmiany w bazie (interakcje)
        verify(walletRepository, times(2)).save(any()); // Czy zapisano 2 portfele?
        verify(transactionRepository, times(1)).save(any()); // Czy zapisano historię?
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughMoney() {
        // --- GIVEN ---
        UUID userId = UUID.randomUUID();

        // Biedny portfel (tylko 100 zł)
        Wallet plnWallet = new Wallet();
        plnWallet.setBalance(new BigDecimal("100.00"));

        // Kurs 4.00
        ExchangeRate rate = new ExchangeRate("USD", new BigDecimal("4.00"));

        when(walletRepository.findByUserIdAndCurrency(userId, "PLN")).thenReturn(Optional.of(plnWallet));
        when(walletRepository.findByUserIdAndCurrency(userId, "USD")).thenReturn(Optional.of(new Wallet())); // USD istnieje
        when(exchangeRateRepository.findByCurrency("USD")).thenReturn(Optional.of(rate));

        ExchangeRequest request = new ExchangeRequest();
        request.setUserId(userId);
        request.setFromCurrency("PLN");
        request.setToCurrency("USD");
        request.setAmount(new BigDecimal("100.00")); // Chcemy kupić 100 USD (koszt 400 PLN)

        // --- WHEN & THEN ---
        // Oczekujemy, że wyskoczy błąd RuntimeException
        assertThrows(RuntimeException.class, () -> {
            exchangeService.exchangeCurrency(request);
        });

        // Upewniamy się, że NIE zapisano zmian w bazie (bo transakcja upadła)
        verify(walletRepository, never()).save(any());
    }
}