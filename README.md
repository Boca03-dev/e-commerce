# E-commerce Mikroservisna Aplikacija

Projekat iz predmeta Projektovanje Distribuiranih Sistema - mikroservisna aplikacija za e-commerce.

## 📋 Tehnologije

- Java 21
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Maven
- H2 Database
- Eureka (Service Discovery)
- Spring Cloud Config (Centralizovana konfiguracija)
- RabbitMQ (Message Broker)
- Spring Cloud Gateway
- OpenFeign
- Resilience4j (Circuit Breaker + Retry)

## 🏗️ Arhitektura

Projekat se sastoji od sledećih servisa:

1. **config-server** (port 8888) - Centralizovano upravljanje konfiguracijom iz `config-repo` direktorijuma ✅
2. **discovery-service** (port 8761) - Eureka Server za service discovery ✅
3. **api-gateway** (port 8080) - API Gateway za rutiranje zahteva ✅
4. **users-service** (port 8081) - Mikroservis za upravljanje korisnicima ✅
5. **orders-service** (port 8082) - Mikroservis za upravljanje porudžbinama (Producer poruka) ✅
6. **notification-service** (port 8083) - Mikroservis za slanje notifikacija (Consumer poruka) ✅

```
                    ┌─────────────────┐
                    │  Eureka Server  │
                    │   (port 8761)   │
                    └────────┬────────┘
                             │
                    ┌────────┴────────┐
                    │   API Gateway   │
                    │   (port 8080)   │ ← CENTRALNA ULAZNA TAČKA
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
      ┌───────▼───────┐            ┌────────▼────────┐
      │ Users Service │            │ Orders Service  │
      │  (port 8081)  │◄───Feign───┤  (port 8082)   │
      └───────────────┘            └─────────────────┘
```

## 🚀 Pokretanje projekta

### Preduslovi
- Java JDK 21 ili noviji
- Maven 3.6+
- IntelliJ IDEA (ili bilo koji drugi IDE)

## 🐳 Docker Compose - Pokreni ceo sistem sa jednom komandom!

### Preduslovi
- Docker instaliran
- Docker Compose instaliran

### Pokreni sve servise u Docker-u:

```bash
# Build svih image-a (prvi put)
docker-compose build

# Pokreni sve servise
docker-compose up

# Ili u pozadini (detached mode)
docker-compose up -d
```

**Provera:**
```bash
docker-compose ps
```

**Pristup servisima:**
- Eureka: http://localhost:8761
- API Gateway: http://localhost:8080
- Users API: http://localhost:8080/api/users
- Orders API: http://localhost:8080/api/orders

**Zaustavi sve:**
```bash
docker-compose down
```


## 🚀 Ključne funkcionalnosti

### 1. Centralizovana Konfiguracija (Spring Cloud Config)
- Svi mikroservisi povlače svoje `application.yml` postavke sa **Config Servera**.
- Konfiguracioni fajlovi se nalaze u eksternom folderu `config-repo`.
- Omogućen je dinamički refresh parametara bez restarta servisa.

### 2. Event-Driven komunikacija (RabbitMQ)
- **Asinhrona obrada**: Kada se kreira porudžbina u `orders-service`, šalje se `OrderCreatedEvent` u RabbitMQ exchange.
- **Notification Service**: Sluša poruke sa queue-a i simulira slanje Email i SMS notifikacija.
- **JSON Serializacija**: Korišćen `Jackson2JsonMessageConverter` sa podrškom za Java 8 `LocalDateTime`.

### 3. Service Discovery & Gateway
- Svi servisi su registrovani na **Eureka** serveru.
- **API Gateway** služi kao jedinstvena ulazna tačka (port 8080) i automatski rutira zahteve ka servisima koristeći Load Balancing.

### 4. Komunikacija i Otpornost (Feign & Resilience4j)
- **Feign Client**: Direktna sinhrona komunikacija između `orders-service` i `users-service`.
- **Circuit Breaker**: Ako `users-service` padne, sistem aktivira fallback mehanizam i sprečava kaskadni otkaz.



## 🎓 Demonstracija Funkcionalnosti

### 1. Service Discovery
- Otvori Eureka Dashboard (http://localhost:8761)
- Vidi registrovane servise (users, orders, gateway)

### 2. CRUD Operacije
- GET svi korisnici: http://localhost:8080/api/users
- POST kreiraj porudžbinu: http://localhost:8080/api/orders

### 3. Komunikacija između servisa (Feign)
- GET http://localhost:8080/api/orders/1/details
- Orders-service poziva users-service i spaja podatke

### 4. Validacija
- POST porudžbina sa nepostojećim userId (999)
- Greška: "User with ID 999 does not exist"

### 5. Circuit Breaker
- Ugasi users-service
- POST nova porudžbina
- Greška: "Users service is currently unavailable..."
- GET /api/orders/1/details - fallback sa "Informacije o korisniku trenutno nisu dostupne"

### 6. API Gateway Rutiranje
- Svi zahtevi idu na port 8080
- Gateway ih rutira ka odgovarajućim servisima
- Logovi pokazuju prolazak kroz gateway


## 📂 Struktura Projekta

```
E-commerce/
├── config-server/        # Spring Cloud Config Server
├── config-repo/          # Folder sa .yml konfiguracijama
├── discovery-service/    # Eureka Server
├── api-gateway/          # Spring Cloud Gateway
├── users-service/        # User management
├── orders-service/       # Order management (RabbitMQ Producer)
├── notification-service/ # Notification handler (RabbitMQ Consumer)
└── docker-compose.yml    # Docker orkestracija
```

## 🔧 Tehnički Detalji

### Portovi
- 8761 - Discovery Service (Eureka)
- 8080 - API Gateway
- 8081 - Users Service
- 8082 - Orders Service

### Baze podataka
- Users Service: H2 (in-memory) - `jdbc:h2:mem:usersdb`
- Orders Service: H2 (in-memory) - `jdbc:h2:mem:ordersdb`

### Resilience4j Konfiguracija
- Circuit Breaker: 50% failure rate threshold, 5 minimum calls
- Retry: 3 pokušaja, exponential backoff (1s, 2s, 4s)
- Wait duration in open state: 10 sekundi

## 👤 Autor

Bogdan Bogicevic - PDS Projekat 2024/2025