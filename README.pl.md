# CoinX - Fintech Core Banking Service

Backendowy system do wymiany walut w czasie rzeczywistym, symulujący operacje bankowe. Aplikacja umożliwia zarządzanie portfelami wielowalutowymi, integruje się z zewnętrznym API (NBP) w celu aktualizacji kursów oraz zapewnia pełną transakcyjność operacji finansowych.

## Główne Funkcjonalności

* **Zarządzanie Portfelem:** Tworzenie użytkowników i portfeli w różnych walutach (PLN, USD, EUR).
* **Wymiana Walut (Exchange):** Logika biznesowa przeliczająca waluty z uwzględnieniem spreadów i salda.
* **Bezpieczeństwo Transakcji:** Zastosowanie `@Transactional` gwarantuje atomowość operacji (ACID) - środki nie zginą w przypadku błędu.
* **Integracja z NBP:** Scheduler (`@Scheduled`) automatycznie pobiera aktualne kursy walut z API Narodowego Banku Polskiego.
* **Audit Log:** Pełna historia transakcji zapisywana w bazie danych.
* **Obsługa Błędów:** Dedykowana obsługa wyjątków biznesowych (np. niewystarczające środki).

## 🛠️ Technologie

* **Java 17**
* **Spring Boot 3** (Web, Data JPA)
* **Hibernate / PostgreSQL**
* **Lombok**
* **JUnit 5 & Mockito** (Testy jednostkowe)
* **Maven**

## Przykłady Użycia (API Endpoints)

### 1. Wymiana Walut
`POST /exchange`
```json
{
  "userId": "uuid-user-id",
  "fromCurrency": "PLN",
  "toCurrency": "USD",
  "amount": 100.00
}
```


