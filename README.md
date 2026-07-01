# Uni Software Architecture Casino Project

## Instant Win Enterprise

A distributed casino application built with Spring Boot, PostgreSQL and Docker.

The system currently consists of multiple services communicating through Docker networking. The Bank service is fully implemented and follows a Vertical Slice Architecture approach. Roulette and Slot Machine services are planned and will be integrated in future iterations.

---

## Contributors

- Mathis Kriwoluzky
- Nikita Schmidt

---

# Architecture

The project follows a Vertical Slice Architecture.

Instead of organizing code by technical layers, features are grouped into independent slices.

Current slices inside the Bank service:

## User Slice

- Create User
- Retrieve User
- Retrieve All Users
- Update User
- Delete User
- Deposit Funds
- Withdraw Funds
- User Existence Validation

## Transaction Slice

- Create Transaction
- Retrieve Transaction History
- Retrieve User Transactions
- Update Transaction
- Delete Transaction

Each slice contains its own:

- Controllers
- Services
- Repositories
- DTOs
- Views
- Utilities

This keeps features self-contained and easier to maintain.

---

# Services & Ports

| Service     | Description          | Host Port | Container Port |
| ----------- | -------------------- | --------- | -------------- |
| bank        | Bank service         | 8081      | 8080           |
| roulette    | Roulette service     | 8082      | 8080           |
| slotmachine | Slot machine service | 8083      | 8080           |

---

# Databases

| Database    | Host Port | DB Name    | Username | Password |
| ----------- | --------- | ---------- | -------- | -------- |
| bank-db     | 5433      | bankdb     | bank     | bank     |
| roulette-db | 5434      | roulettedb | roulette | roulette |
| slot-db     | 5435      | slotdb     | slot     | slot     |

---

# Service Communication

Services communicate internally through Docker network names.

### Roulette → Bank

```text
http://bank:8080
```

### Slot Machine → Bank

```text
http://bank:8080
```

---

# Project Setup

## Prerequisites

Required software:

- Docker
- Docker Compose

Verify installation:

```bash
docker --version
docker compose version
```

---

## Start the Project

Build and start all services:

```bash
docker compose up --build
```

---

## Stop the Project

```bash
docker compose down
```

Remove containers and volumes:

```bash
docker compose down -v
```

---

# Persistent Storage

The following Docker volumes are used:

- bank-db-data
- roulette-db-data
- slot-db-data

Database data persists across container restarts.

---

# Service Dependencies

- bank depends on bank-db
- roulette depends on roulette-db and bank
- slotmachine depends on slot-db and bank

---

# Access

## APIs

### Bank Service

```text
http://localhost:8081
```

### Roulette Service

```text
http://localhost:8082
```

### Slot Machine Service

```text
http://localhost:8083
```

---

# Swagger / OpenAPI

The Bank service exposes an OpenAPI specification and Swagger UI.

### Swagger UI

Open in your browser:

```text
http://localhost:8081/swagger-ui/index.html
```

### OpenAPI JSON

```text
http://localhost:8081/v3/api-docs
```

Swagger can be used to:

- Inspect available endpoints
- View request and response models
- Test endpoints directly from the browser
- Validate request payloads

---

# PostgreSQL Connections

Exam`

---

# Current Project Status

## Completed

### Bank Service

- User Slice
- Transaction Slice
- PostgreSQL Persistence
- Docker Integration
- Swagger / OpenAPI Documentation

## Planned

### Roulette Service

Currently under development.

### Slot Machine Service

Currently under development.
ple connection for the Bank database:

```text
Host: localhost
Port: 5433
Database: bankdb
Username: bank
Password: bank
``