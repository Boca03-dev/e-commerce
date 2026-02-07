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
- Resilience4j

## 🏗️ Arhitektura

Projekat se sastoji od sledećih servisa:

1. **discovery-service** (port 8761) - Eureka Server za service discovery
2. **users-service** (port 8081) - Mikroservis za upravljanje korisnicima
3. **orders-service** (port 8082) - Mikroservis za upravljanje porudžbinama
4. **api-gateway** (port 8080) - API Gateway za rutiranje zahteva

## 🚀 Pokretanje projekta

### Preduslovi
- Java JDK 21 ili noviji
- Maven 3.6+
- IntelliJ IDEA (ili bilo koji drugi IDE)

### Korak po korak (VAŽAN REDOSLED!)

#### 1. Discovery Service (Eureka Server)

```bash
cd discovery-service
mvn clean install
mvn spring-boot:run
```

Eureka Dashboard će biti dostupan na: http://localhost:8761

#### 2. Users Service

```bash
cd users-service
mvn clean install
mvn spring-boot:run
```

Users API će biti dostupan na: http://localhost:8081/api/users

**Provera registracije:** Idi na http://localhost:8761 i proveri da li se `USERS-SERVICE` pojavio u listi.

#### 3. Orders Service

```bash
cd orders-service
mvn clean install
mvn spring-boot:run
```

Orders API će biti dostupan na: http://localhost:8082/api/orders

**Provera:** Oba servisa treba da budu vidljiva na Eureka Dashboard-u.

## 🎯 Ključne Funkcionalnosti

### Users Service
- CRUD operacije za korisnike
- Validacija podataka
- H2 in-memory baza
- REST API endpoints

### Orders Service
- CRUD operacije za porudžbine
- **Feign Client** - komunikacija sa users-service
- **Resilience4j Circuit Breaker** - otpornost na greške
- **Retry mehanizam** - automatsko ponavljanje
- **Agregacioni endpoint** `/orders/{id}/details` - spaja podatke Order + User
- Validacija userId pre kreiranja porudžbine
- Fallback metode kada users-service ne radi


**Glavna demonstracija:**
```
GET http://localhost:8082/api/orders/1/details
```
Ovaj endpoint pokazuje komunikaciju između servisa - dohvata porudžbinu iz orders-service i korisnika iz users-service, spaja ih i vraća kompletan odgovor.

## 👤 Autor

Bogdan Bogicevic - PDS Projekat 2024/2025
