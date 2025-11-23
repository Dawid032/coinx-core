# CoinX - Fintech Core Banking Service

A backend system for real-time currency exchange, simulating core banking operations. The application enables multi-currency wallet management, integrates with an external API (National Bank of Poland) for live rate updates, and ensures full transaction integrity using ACID principles.

## Key Features

* **Wallet Management:** Creation of users and multi-currency wallets (PLN, USD, EUR).
* **Currency Exchange Engine:** Business logic handling exchange rates calculations and balance validation using precise `BigDecimal` math.
* **Transaction Safety:** Implementation of `@Transactional` ensures atomic operations (ACID) — funds are secure and consistent even in case of system errors.
* **External API Integration:** A Scheduler (`@Scheduled`) automatically fetches current exchange rates from the NBP API (National Bank of Poland).
* **Audit Log:** Complete transaction history is persisted in the database for auditing purposes.
* **Error Handling:** Custom handling of business exceptions (e.g., insufficient funds, invalid currency).

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot 3** (Web, Data JPA)
* **Hibernate / PostgreSQL**
* **Lombok**
* **JUnit 5 & Mockito** (Unit Testing)
* **Maven**

## API Usage Examples

### 1. Perform Currency Exchange
`POST /exchange`
```json
{
  "userId": "uuid-user-id",
  "fromCurrency": "PLN",
  "toCurrency": "USD",
  "amount": 100.00
}
```