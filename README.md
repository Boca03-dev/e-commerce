# E-commerce Mikroservisna Aplikacija

Projekat iz predmeta Projektovanje Distribuiranih Sistema - mikroservisna aplikacija za e-commerce.

## 📋 Tehnologije

- Java 21
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Maven
- H2 Database
- Eureka (Service Discovery)
- Spring Cloud Gateway
- OpenFeign
- Resilience4j (Circuit Breaker + Retry)

## 🏗️ Arhitektura

Projekat se sastoji od sledećih servisa:

1. **discovery-service** (port 8761) - Eureka Server za service discovery ✅
2. **users-service** (port 8081) - Mikroservis za upravljanje korisnicima ✅
3. **orders-service** (port 8082) - Mikroservis za upravljanje porudžbinama ✅
4. **api-gateway** (port 8080) - API Gateway za rutiranje zahteva ✅

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

### Korak po korak (VAŽAN REDOSLED!)

#### 1. Discovery Service (Eureka Server)

```bash
cd discovery-service
mvn clean install
mvn spring-boot:run
```

Eureka Dashboard: http://localhost:8761

#### 2. Users Service

```bash
cd users-service
mvn clean install
mvn spring-boot:run
```

Users API: http://localhost:8081/api/users

#### 3. Orders Service

```bash
cd orders-service
mvn clean install
mvn spring-boot:run
```

Orders API: http://localhost:8082/api/orders

#### 4. API Gateway

```bash
cd api-gateway
mvn clean install
mvn spring-boot:run
```

Gateway: http://localhost:8080

**Provera:** Sva 3 servisa (users, orders, gateway) treba da budu vidljiva na Eureka Dashboard-u (http://localhost:8761).



## 🎯 Ključne Funkcionalnosti

### Discovery Service (Eureka)
- Service registry - svi servisi se registruju ovde
- Service discovery - servisi pronalaze jedni druge
- Dashboard za monitoring

### Users Service
- **CRUD operacije** za korisnike (GET, POST, PUT, DELETE)
- **Validacija podataka** (email format, obavezna polja)
- **H2 in-memory baza** sa test podacima
- **REST API endpoints:** `/api/users/**`

### Orders Service
- **CRUD operacije** za porudžbine (GET, POST, PUT, DELETE)
- **Feign Client** - komunikacija sa users-service
- **Resilience4j Circuit Breaker** - otpornost na greške
- **Retry mehanizam** - automatsko ponovljavanje (3x)
- **Agregacioni endpoint** `/api/orders/{id}/details` - spaja Order + User podatke
- **Validacija userId** - proverava da li korisnik postoji pre kreiranja porudžbine
- **Fallback metode** - kada users-service ne radi
- **REST API endpoints:** `/api/orders/**`

### API Gateway
- **Centralna ulazna tačka** - svi zahtevi kroz port 8080
- **Rutiranje zahteva** ka mikroservisima
- **Load balancing** preko Eureka
- **CORS konfiguracija**
- **Logging filter** - loguje sve zahteve
- **Actuator endpoints** za monitoring


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
├── discovery-service/     # Eureka Server
├── users-service/         # User management
├── orders-service/        # Order management + Feign + Resilience4j
├── api-gateway/          # API Gateway
└── README.md
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