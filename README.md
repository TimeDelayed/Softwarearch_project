# Uni Softwarearch. casino project 
## Instant Win Enterprise
## Contributors: 
    -   Mathis Kriwoluzky
    -   Nikita Schmidt

# Project Setup

## Services & Ports

| Service     | Description          | Host Port | Container Port |
| ----------- | -------------------- | --------- | -------------- |
| bank        | Bank service         | 8081      | 8080           |
| roulette    | Roulette service     | 8082      | 8080           |
| slotmachine | Slot machine service | 8083      | 8080           |

## Databases

| Database    | Host Port | DB Name    | Username | Password |
| ----------- | --------- | ---------- | -------- | -------- |
| bank-db     | 5433      | bankdb     | bank     | bank     |
| roulette-db | 5434      | roulettedb | roulette | roulette |
| slot-db     | 5435      | slotdb     | slot     | slot     |

## Service Communication

Services communicate internally via Docker network names:

* Roulette → Bank:

  * `http://bank:8080`

* Slotmachine → Bank:

  * `http://bank:8080`

## Start the Project

```bash
docker compose up --build
```

## Persistent Storage

The following Docker volumes are used:

* `bank-db-data`
* `roulette-db-data`
* `slot-db-data`

This ensures database data persists after container restarts.

## Dependencies

* `bank` depends on `bank-db`
* `roulette` depends on `roulette-db` and `bank`
* `slotmachine` depends on `slot-db` and `bank`

## Access

### APIs

* Bank API:

  * `http://localhost:8081`

* Roulette API:

  * `http://localhost:8082`

* Slotmachine API:

  * `http://localhost:8083`

### PostgreSQL Connections

Example for the Bank database:

```text
Host: localhost
Port: 5433
Database: bankdb
Username: bank
Password: bank
```
