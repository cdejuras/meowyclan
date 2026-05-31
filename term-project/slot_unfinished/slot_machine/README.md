# 🎰 Lucky Spins — Slot Machine App

A fullstack Java slot machine game with Spring Boot backend + plain HTML/CSS/JS frontend + PostgreSQL database.

---

## Project Structure
```
slot_machine/
├── backend/slotmachine/     ← Spring Boot (Java 21, Gradle)
│   └── src/main/java/com/slotmachine/
│       ├── SlotmachineApplication.java   ← App entry point
│       ├── entity/Slot.java              ← DB entity (session)
│       ├── repository/SlotRepository.java
│       ├── dto/SlotDto.java              ← Data Transfer Object
│       ├── mapper/SlotMapper.java        ← Entity ↔ DTO converter
│       ├── payment/
│       │   ├── PaymentProcessor.java     ← Interface (Abstraction)
│       │   └── CashPaymentProcessor.java ← Impl (Inheritance/Polymorphism)
│       ├── service/
│       │   ├── SlotMachine.java          ← Game logic & odds (Encapsulation)
│       │   └── SlotService.java          ← Business logic
│       ├── controller/SlotController.java ← REST API
│       ├── request/SlotRequest.java
│       └── responses/SlotResponse.java
└── frontend/
    └── index.html                        ← Full UI (no framework needed)
```

---

## 4 OOP Principles Demonstrated

| Principle      | Where                                                         |
|----------------|---------------------------------------------------------------|
| Encapsulation  | `Slot.java` (private fields + getters/setters), `SlotMachine.java` (hidden spin logic) |
| Abstraction    | `PaymentProcessor.java` (interface hides payment details)     |
| Inheritance    | `CashPaymentProcessor implements PaymentProcessor`            |
| Polymorphism   | `SlotService` uses `PaymentProcessor` type — swap implementation freely |

---

## Features
- 🎰 Slot machine with weighted symbol odds (Cherry=most common, Diamond/7=rarest)
- 🏆 Jackpot (3-match) → **5× payout** | Two-match → **1.5× payout**
- 💰 Deposit (₱100 min, ₱10,000 max) and Withdraw
- 📊 Leaderboard with spin count & wins saved to PostgreSQL
- 🎨 Upload custom image symbols to replace defaults
- 🗄️ PostgreSQL: spins & wins saved per session

---

## Setup

### 1. PostgreSQL
```sql
CREATE DATABASE slotmachine_db;
```
Update `application.properties`:
```
spring.datasource.username=postgres
spring.datasource.password=your_password
```
Tables are created automatically by Hibernate (`ddl-auto=update`).

### 2. Run Backend
```bash
cd backend/slotmachine
./gradlew bootRun
```
Backend runs on http://localhost:8080

### 3. Open Frontend
Open `frontend/index.html` in your browser (or serve with Live Server).

---

## API Endpoints

| Method | URL                              | Description            |
|--------|----------------------------------|------------------------|
| POST   | /api/slots/session               | Create session + deposit |
| GET    | /api/slots/session/{id}          | Get session            |
| GET    | /api/slots/sessions              | All sessions (leaderboard) |
| DELETE | /api/slots/session/{id}          | Delete session         |
| POST   | /api/slots/session/{id}/spin     | Spin the reels         |
| POST   | /api/slots/session/{id}/deposit  | Add funds              |
| POST   | /api/slots/session/{id}/withdraw | Withdraw funds         |
| POST   | /api/slots/symbols/upload        | Upload custom symbol   |
| GET    | /api/slots/symbols               | List uploaded symbols  |
| DELETE | /api/slots/symbols               | Delete a symbol        |
