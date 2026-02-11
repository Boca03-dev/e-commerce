# E-commerce Mikroservisna Aplikacija

Projekat iz predmeta Programiranje distribuiranih sistema - kompletan mikroservisni sistem za e-commerce sa naprednim funkcionalnostima.

## 📋 Tehnologije

- Java 21
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Maven
- H2 Database
- Eureka (Service Discovery)
- Spring Cloud Config Server (Centralizovana konfiguracija)
- Spring Cloud Gateway (API Gateway sa autentifikacijom)
- RabbitMQ (Message Broker - Event-Driven)
- WebSocket (Real-time notifikacije)
- OpenFeign (Service-to-Service komunikacija)
- Resilience4j (Circuit Breaker + Retry)
- Docker Compose (Orkestacija)

---

## 🏗️ Arhitektura

Projekat se sastoji od sledećih servisa:

1. **config-server** (port 8888) - Spring Cloud Config Server - centralizovana konfiguracija ✅
2. **discovery-service** (port 8761) - Eureka Server za service discovery ✅
3. **api-gateway** (port 8080) - API Gateway sa API Key autentifikacijom ✅
4. **users-service** (port 8081) - Mikroservis za upravljanje korisnicima ✅
5. **orders-service** (port 8082) - Mikroservis za porudžbine (RabbitMQ Producer) ✅
6. **notification-service** (port 8083) - Mikroservis za notifikacije (RabbitMQ Consumer + WebSocket) ✅

```
                    ┌─────────────────┐
                    │  Config Server  │ :8888
                    │   (Git Repo)    │
                    └────────┬────────┘
                             │
                    ┌────────┴────────┐
                    │  Eureka Server  │ :8761
                    └────────┬────────┘
                             │
                    ┌────────┴────────┐
                    │   API Gateway   │ :8080
                    │ (API Key Auth)  │ ← CENTRALNA ULAZNA TAČKA
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
      ┌───────▼───────┐            ┌────────▼────────┐
      │ Users Service │            │ Orders Service  │
      │  (port 8081)  │◄───Feign───┤  (port 8082)   │
      └───────────────┘            └────────┬────────┘
                                            │
                                            ▼
                                    ┌───────────────┐
                                    │   RabbitMQ    │
                                    │ Message Broker│
                                    └───────┬───────┘
                                            │
                                            ▼
                                  ┌──────────────────┐
                                  │ Notification     │
                                  │ Service :8083    │
                                  │ (WebSocket)      │
                                  └─────────┬────────┘
                                            │
                                            ▼
                                    ┌───────────────┐
                                    │   Browser     │
                                    │ (Real-time!)  │
                                    └───────────────┘
```

---

## 🚀 Pokretanje projekta

### Preduslovi
- Java JDK 21 ili noviji
- Maven 3.6+
- Docker & Docker Compose
- IntelliJ IDEA (opciono)

---

## 🐳 Docker Compose - Pokreni ceo sistem sa jednom komandom!

### Preduslovi
- Docker instaliran (https://www.docker.com/products/docker-desktop/)

### Build i pokreni sve servise:

```bash
# Build svih image-a (prvi put, traje ~10-15 min)
docker-compose build

# Pokreni sve servise
docker-compose up

# Ili u pozadini (detached mode)
docker-compose up -d

# Provera statusa
docker-compose ps

# Logovi
docker-compose logs -f

# Zaustavi sve
docker-compose down
```

---

### Pristup servisima:

| Servis | URL | Opis |
|--------|-----|------|
| **Eureka Dashboard** | http://localhost:8761 | Service registry |
| **API Gateway** | http://localhost:8080 | Centralna ulazna tačka |
| **Config Server** | http://localhost:8888 | Centralizovana konfiguracija |
| **RabbitMQ Management** | http://localhost:15672 | Message broker (admin/admin) |
| **Users API** | http://localhost:8080/api/users | CRUD korisnici |
| **Orders API** | http://localhost:8080/api/orders | CRUD porudžbine |
| **Health Check** | http://localhost:8080/actuator/health | System health |

---

## 🎯 Ključne Funkcionalnosti

### 1. ⚙️ Centralizovana Konfiguracija (Spring Cloud Config)

**Šta radi:**
- Svi mikroservisi povlače konfiguraciju sa Config Server-a
- Konfiguracioni fajlovi u Git repository-ju (`config-repo/`)
- Refresh konfiguracije bez restarta servisa

**Testiranje:**
```bash
# Proveri config za users-service
curl http://localhost:8888/users-service/default

# Refresh konfiguracije (posle izmene u config-repo)
curl -X POST http://localhost:8081/actuator/refresh
```

**Fajlovi:**
- `config-repo/application.yml` - zajednička konfiguracija
- `config-repo/users-service.yml` - specifična za users
- `config-repo/orders-service.yml` - specifična za orders
- `config-repo/api-gateway.yml` - specifična za gateway

---

### 2. 🐰 Event-Driven Arhitektura (RabbitMQ)

**Šta radi:**
- **Asinhrona komunikacija** između servisa
- Kada se kreira porudžbina → `orders-service` šalje `OrderCreatedEvent` na RabbitMQ
- `notification-service` konzumira event i šalje notifikacije

**Testiranje:**
```bash
# Kreiraj porudžbinu
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "X-API-Key: ecommerce-secret-key-123" \
  -d '{
    "userId": 1,
    "productName": "Event Test Product",
    "quantity": 1,
    "price": 99.99
  }'

# Proveri RabbitMQ Dashboard
# http://localhost:15672 → Queues → order.notifications.queue

# Proveri notification-service logove
docker-compose logs notification-service
```

**Konfiguracija:**
- Exchange: `ecommerce.exchange` (Topic)
- Queue: `order.notifications.queue`
- Routing Key: `order.created`

---

### 3. 📡 Real-Time Notifikacije (WebSocket)

**Šta radi:**
- **Real-time** poruke na browser bez refresha
- Kada se kreira porudžbina → browser **ODMAH** dobija notifikaciju

**Demo:**
1. Otvori `realtime-dashboard.html` u browser-u
2. Vidiš: "🟢 Connected"
3. Kreiraj porudžbinu u Postman-u
4. **Notifikacija se ODMAH pojavljuje na dashboard-u!**

**WebSocket Endpoint:**
- Konekcija: `ws://localhost:8083/ws`

---

### 4. 🔐 API Key Autentifikacija (API Gateway)

**Šta radi:**
- **Svi zahtevi** moraju imati validan API key u header-u
- Bezbedni endpoint-i zahtevaju autentifikaciju
- Javni endpoint-i (health, actuator) dostupni bez key-a

**Validni API Ključevi:**
```
ecommerce-secret-key-123
admin-key-456
test-key-789
```

**Testiranje:**

❌ **Bez API key:**
```bash
curl http://localhost:8080/api/users
# → 401 Unauthorized
```

✅ **Sa validnim API key:**
```bash
curl -H "X-API-Key: ecommerce-secret-key-123" \
     http://localhost:8080/api/users
# → 200 OK
```

✅ **Javni endpoint (bez key-a):**
```bash
curl http://localhost:8080/actuator/health
# → 200 OK
```

**Postman Setup:**
- Collection → Edit → Authorization
- Type: API Key
- Key: `X-API-Key`
- Value: `ecommerce-secret-key-123`
- Add to: Header

---

### 5. 🌐 API Gateway & Service Discovery

**Šta radi:**
- **Centralna ulazna tačka** - svi zahtevi kroz port 8080
- **Load balancing** preko Eureka
- **Rutiranje** zahteva ka mikroservisima
- **CORS** konfiguracija
- **Logging** svih zahteva

**Rute:**
```
/api/users/**   → users-service
/api/orders/**  → orders-service
/actuator/**    → javno
```

**Eureka Dashboard:**
http://localhost:8761 - vidi sve registrovane servise

---

### 6. 🔄 Service-to-Service komunikacija (OpenFeign)

**Šta radi:**
- `orders-service` poziva `users-service` preko Feign Client-a
- Agregacioni endpoint spaja podatke iz oba servisa

**Testiranje:**
```bash
# Agregacioni endpoint
curl -H "X-API-Key: ecommerce-secret-key-123" \
     http://localhost:8080/api/orders/1/details
```

**Odgovor:**
```json
{
  "orderId": 1,
  "productName": "Laptop",
  "quantity": 1,
  "price": 999.99,
  "status": "PENDING",
  "user": {
    "id": 1,
    "firstName": "Marko",
    "lastName": "Marković",
    "email": "marko.markovic@example.com"
  }
}
```

---

### 7. 🛡️ Otpornost Sistema (Resilience4j)

**Circuit Breaker:**
- Prati uspešnost poziva ka `users-service`
- Ako 50%+ poziva ne uspe → prelazi u **OPEN** stanje
- U OPEN stanju → odmah vraća fallback (ne pokušava poziv)
- Posle 10s → prelazi u **HALF_OPEN** i testira 3 poziva

**Retry:**
- 3 pokušaja sa eksponencijalnim backoff-om (1s, 2s, 4s)

**Testiranje:**
```bash
# Ugasi users-service
docker-compose stop users-service

# Agregacioni endpoint
curl -H "X-API-Key: ecommerce-secret-key-123" \
     http://localhost:8080/api/orders/1/details
  
```

---


## 📂 Struktura Projekta

```
E-commerce/
├── config-server/          # Spring Cloud Config Server
├── config-repo/            # Git repository sa konfiguracijama
│   ├── application.yml
│   ├── users-service.yml
│   ├── orders-service.yml
│   └── api-gateway.yml
├── discovery-service/      # Eureka Server
├── api-gateway/            # API Gateway + API Key Auth
├── users-service/          # User management
├── orders-service/         # Order management + RabbitMQ Producer
├── notification-service/   # RabbitMQ Consumer + WebSocket
├── realtime-dashboard.html # Frontend za real-time notifikacije
└── docker-compose.yml      # Docker orkestacija
```

---

## 🔧 Tehnički Detalji

### Portovi

| Port | Servis |
|------|--------|
| 8888 | Config Server |
| 8761 | Eureka (Discovery) |
| 8080 | API Gateway |
| 8081 | Users Service |
| 8082 | Orders Service |
| 8083 | Notification Service |
| 5672 | RabbitMQ (AMQP) |
| 15672 | RabbitMQ Management UI |

### Baze podataka
- Users Service: H2 in-memory (`jdbc:h2:mem:usersdb`)
- Orders Service: H2 in-memory (`jdbc:h2:mem:ordersdb`)

### Resilience4j Konfiguracija
- **Circuit Breaker:** 50% failure threshold, 5 min calls, 10s wait
- **Retry:** 3 attempts, exponential backoff (1s, 2s, 4s)

### RabbitMQ
- **Exchange:** `ecommerce.exchange` (Topic)
- **Queue:** `order.notifications.queue`
- **Routing Key:** `order.created`

---

## 👤 Autor

**Bogdan Bogicevic 28/2022** - PDS Projekat 2024/2025
