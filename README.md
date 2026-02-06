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

### Korak po korak

#### 1. Discovery Service (Eureka Server)

```bash
cd discovery-service
mvn clean install
mvn spring-boot:run
```

Eureka Dashboard će biti dostupan na: http://localhost:8761

## 📝 Status razvoja

- [x] Discovery Service (Eureka)
- [ ] Users Service
- [ ] Orders Service
- [ ] API Gateway
- [ ] Resilience4j (Circuit Breaker + Retry)
- [ ] Agregacioni endpoint

## 👤 Autor

Bogdan Bogicevic - PDS Projekat 2024/2025
