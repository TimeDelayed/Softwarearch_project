# InstantWin Enterprise

InstantWin Enterprise ist eine REST-basierte Microservice-Anwendung, die im Rahmen des Moduls
**Softwareengineering und Softwarearchitekturen** entstanden ist. Das System bildet eine kleine
Casino-Plattform mit einem Bank-Service sowie den Spielen Roulette und Slotmachine ab. Gespielt
wird ausschließlich mit virtuellem Guthaben.

Der Name ist dabei bewusst etwas ironisch gewählt: Im Beleg geht es ausdrücklich nicht darum,
Glücksspiel zu verherrlichen, sondern anhand von Gewinnchancen, House Edge sowie Gewinn- und
Verluststatistiken zu zeigen, dass ein Casino langfristig im Vorteil ist. `InstantWin Enterprise`
klingt wie der passende Marketingname eines Unternehmens, das genau diesen Umstand möglichst
freundlich verkaufen möchte.

## Autoren

- Mathis Kriwoluzky
- Nikita Schmidt

## Inhalt

- [Schnellstart](#schnellstart)
- [Services, Ports und Datenbanken](#services-ports-und-datenbanken)
- [Swagger und OpenAPI](#swagger-und-openapi)
- [API-Übersicht](#api-übersicht)
- [Konfiguration der Spiele](#konfiguration-der-spiele)
- [Architektur](#architektur)
- [Tests](#tests)
- [Docker-Images für die Abgabe](#docker-images-für-die-abgabe)
- [Bewusste Abweichungen und technische Schulden](#bewusste-abweichungen-und-technische-schulden)
- [Disclaimer](#disclaimer)
- [Lizenz](#lizenz)

## Schnellstart

### Voraussetzungen

Für den normalen Start müssen lokal nur folgende Programme vorhanden sein:

- Docker Engine beziehungsweise Docker Desktop
- Docker Compose als Docker-Plugin
- optional Git, wenn das Projekt nicht als ZIP vorliegt

Java, Maven und PostgreSQL müssen für den Docker-Betrieb nicht separat installiert werden. Maven
und Java 21 werden innerhalb der Docker-Builds verwendet, die drei PostgreSQL-Datenbanken laufen
ebenfalls in Containern.

Die Installation kann so geprüft werden:

```bash
docker --version
docker compose version
```

### Projekt starten

Repository klonen oder das abgegebene ZIP entpacken, anschließend in den Projektordner wechseln.
Die fertig gebauten Images der drei Kern-Services liegen öffentlich in der
[Docker Registry](https://hub.docker.com/r/timedelay/instantwin-enterprise). Sie werden zusammen mit
den benötigten PostgreSQL-Images heruntergeladen und anschließend ohne lokalen Neubuild gestartet:

```bash
docker compose pull
docker compose up --no-build
```

Für einen Start im Hintergrund:

```bash
docker compose up --no-build -d
```

Alternativ können alle Images direkt aus dem Quellcode neu gebaut werden:

```bash
docker compose up --build
```

Beim lokalen Build werden auch die Unit-Tests der drei Services ausgeführt. Der erste Build kann daher
etwas länger dauern, weil Maven- und Docker-Abhängigkeiten heruntergeladen werden müssen.

Status und Logs lassen sich anschließend so prüfen:

```bash
docker compose ps
docker compose logs -f
```

### Projekt stoppen

Container stoppen und entfernen, Daten aber behalten:

```bash
docker compose down
```

Container und persistierte Daten vollständig entfernen:

```bash
docker compose down -v
```

Der zweite Befehl löscht auch alle Benutzer, Transaktionen und gespeicherten Spiele aus den
Docker-Volumes.

## Services, Ports und Datenbanken

Jeder Kern-Service läuft intern auf Port `8080`. Docker Compose veröffentlicht die Services auf
unterschiedlichen Host-Ports, damit die komplette Anwendung gleichzeitig lokal laufen kann.

| Service | Aufgabe | Öffentliche Basis-URL | Container-Port |
| --- | --- | --- | --- |
| Bank | Benutzer, Transaktionen und berechnete Kontostände | `http://localhost:8081` | `8080` |
| Roulette | Roulette-Spiel, Regeln, Chancen und Statistiken | `http://localhost:8082` | `8080` |
| Slotmachine | 3x1-Slot, Regeln, Chancen und Statistiken | `http://localhost:8083` | `8080` |

Jeder Service besitzt eine eigene PostgreSQL-Datenbank. Ein direkter Zugriff ist für die Benutzung
der APIs nicht erforderlich, für die lokale Kontrolle sind die Ports aber veröffentlicht.

| Container | Host-Port | Datenbank | Benutzer | Passwort |
| --- | ---: | --- | --- | --- |
| `bank-db` | `5433` | `bankdb` | `bank` | `bank` |
| `roulette-db` | `5434` | `roulettedb` | `roulette` | `roulette` |
| `slot-db` | `5435` | `slotdb` | `slot` | `slot` |

Die Daten werden in den Volumes `bank-db-data`, `roulette-db-data` und `slot-db-data` gespeichert.

### Laufzeitkonfiguration

Die wesentliche Laufzeitkonfiguration befindet sich in der `compose.yml`. Dabei werden die
Spring-Properties über Umgebungsvariablen gesetzt:

| Variable | Zweck |
| --- | --- |
| `SPRING_DATASOURCE_URL` | JDBC-Verbindung zur jeweils eigenen Datenbank |
| `SPRING_DATASOURCE_USERNAME` | Datenbankbenutzer |
| `SPRING_DATASOURCE_PASSWORD` | Datenbankpasswort |
| `SPRING_JPA_HIBERNATE_DDL_AUTO=update` | Tabellen beim Start automatisch anlegen beziehungsweise aktualisieren |
| `BANK_SERVICE_URL=http://bank:8080` | Interne Adresse des Bank-Service für Roulette, Slotmachine und die Bank-Selbstaufrufe |

Innerhalb des Compose-Netzwerks werden keine Host-Ports verwendet. Roulette und Slotmachine
erreichen die Bank über den Docker-DNS-Namen `http://bank:8080`. Die Werte in der Compose-Datei
sind reine Entwicklungswerte und sollten in einer produktiven Umgebung durch Secrets und eine
passende Migrationsstrategie ersetzt werden.

## Swagger und OpenAPI

Alle drei Kern-Services stellen Swagger UI und die generierte OpenAPI-Beschreibung bereit:

| Service | Swagger UI | OpenAPI JSON |
| --- | --- | --- |
| Bank | [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html) | [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs) |
| Roulette | [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html) | [http://localhost:8082/v3/api-docs](http://localhost:8082/v3/api-docs) |
| Slotmachine | [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html) | [http://localhost:8083/v3/api-docs](http://localhost:8083/v3/api-docs) |

Über Swagger können die Request Bodies eingesehen und Endpunkte direkt ausprobiert werden. Für
einen normalen Ablauf sollte zuerst über die Bank ein Benutzer erstellt und dessen ID anschließend
für Roulette oder Slotmachine verwendet werden.

## API-Übersicht

### Namenskonventionen

Die Aufgabenstellung verwendet Pfade unter `/casino/...`. In diesem Projekt heißt das System jedoch
`InstantWin Enterprise`, weshalb die APIs unter `/instantwin/...` liegen. Zusätzlich laufen die
Services nicht auf den in der Aufgabenstellung beispielhaft verwendeten Ports, sondern getrennt auf
`8081` bis `8083`.

JSON-Felder werden im gesamten Projekt in `camelCase` geschrieben. Dadurch heißen Felder zum
Beispiel `firstName`, `invoicingParty`, `betAmount`, `ballPosition` und `slotStates` statt
`first_name`, `invoicing_party`, `bet_amount`, `ball_position` und `slot_states`. Das weicht von den
Beispielen des Belegs ab, ist innerhalb der Anwendung aber bewusst einheitlich gehalten und passt zu
den üblichen Java-Namenskonventionen.

### Bank-Service

Basis-Pfad: `http://localhost:8081/instantwin/bank/api`

| Methode | Pfad | Zweck |
| --- | --- | --- |
| `GET` | `/users` | Alle Benutzer inklusive berechnetem Kontostand abrufen |
| `GET` | `/user/{id}` | Benutzer inklusive Kontostand abrufen |
| `GET` | `/user/{id}/exists` | Benutzer ohne Kontostandsberechnung auf Existenz prüfen |
| `POST` | `/user` | Benutzer erstellen |
| `PUT` | `/user/{id}` | Vor- und Nachnamen aktualisieren |
| `DELETE` | `/user/{id}` | Benutzer löschen |
| `POST` | `/user/{id}/deposit/{amount}/{decimals}` | Einzahlung als Transaktion anlegen |
| `POST` | `/user/{id}/withdraw/{amount}/{decimals}` | Auszahlung als Transaktion anlegen |
| `GET` | `/transactions` | Alle Transaktionen abrufen |
| `GET` | `/transactions/user/{userId}` | Transaktionen eines Benutzers abrufen |
| `POST` | `/transaction/user/{userId}` | Transaktion für einen Benutzer erstellen |
| `PUT` | `/transaction/{transactionId}` | Transaktion vollständig aktualisieren |
| `DELETE` | `/transaction/{transactionId}` | Transaktion löschen |

Beispiel zum Erstellen eines Benutzers:

```json
{
  "firstName": "Max",
  "lastName": "Mustermann"
}
```

Beispiel zum Erstellen einer Transaktion:

```json
{
  "amount": 25.50,
  "invoicingParty": "ROULETTE"
}
```

Erlaubte Werte für `invoicingParty` sind `USER_SLICE`, `ROULETTE` und `SLOTS`. Positive Beträge
erhöhen den Kontostand, negative Beträge reduzieren ihn. Der Kontostand selbst wird nicht als eigene
veränderbare Spalte geführt, sondern aus allen Transaktionen des Benutzers berechnet.

### Roulette-Service

Basis-Pfad: `http://localhost:8082/instantwin/roulette/api`

| Methode | Pfad | Zweck |
| --- | --- | --- |
| `POST` | `/play` | Runde spielen und Nettoergebnis über die Bank buchen |
| `GET` | `/info/rules` | Spielregeln als Text abrufen |
| `GET` | `/info/chances` | Gewinnchancen und Auszahlungen als Text abrufen |
| `GET` | `/stats` | Globale Roulette-Statistik abrufen |
| `GET` | `/stats/user/{user_id}` | Statistik eines Spielers abrufen |
| `GET` | `/stats/games` | Alle gespeicherten Spiele abrufen |
| `GET` | `/stat/{game_id}` | Einzelnes Spiel abrufen |
| `DELETE` | `/stat/{game_id}` | Einzelnes Spiel löschen |

Beispiel für eine Runde:

```json
{
  "user": 1,
  "betAmount": 10.00,
  "betNumber": 17,
  "betType": "STRAIGHT_UP"
}
```

`amount` in der Antwort ist das Nettoergebnis `Auszahlung - Einsatz`. Ein positiver Wert ist ein
Gewinn, ein negativer Wert ein Verlust. Die Kugelposition wird als `ballPosition` ausgegeben.

### Slotmachine-Service

Basis-Pfad: `http://localhost:8083/instantwin/slots/api`

| Methode | Pfad | Zweck |
| --- | --- | --- |
| `POST` | `/play` | Slot-Runde spielen und Nettoergebnis über die Bank buchen |
| `GET` | `/info/rules` | Spielregeln und Paytable als Text abrufen |
| `GET` | `/info/chances` | Symbol- und Gewinnwahrscheinlichkeiten als Text abrufen |
| `GET` | `/stats` | Globale Slot-Statistik abrufen |
| `GET` | `/stats/user/{userId}` | Statistik eines Spielers abrufen |
| `GET` | `/stats/games` | Alle gespeicherten Spiele abrufen |
| `GET` | `/stat/{gameId}` | Einzelnes Spiel abrufen |
| `DELETE` | `/stat/{gameId}` | Einzelnes Spiel löschen |

Beispiel für eine Runde:

```json
{
  "userId": 1,
  "betAmount": 10.00
}
```

Auch hier beschreibt `amount` das Nettoergebnis und `slotStates` enthält die drei gezogenen Symbole.

## Konfiguration der Spiele

### Roulette

Das Roulette verwendet ein europäisches Rad mit den Zahlen `0` bis `36`. Die Gewinnzahl wird mit
`SecureRandom` erzeugt. Unterstützt werden folgende Wettarten:

| Wettart | Abdeckung | Bruttoauszahlung |
| --- | ---: | ---: |
| `STRAIGHT_UP` | eine Zahl | Einsatz x 36 |
| `SPLIT` | zwei benachbarte Zahlen | Einsatz x 18 |
| `STREET` | drei Zahlen | Einsatz x 12 |
| `CORNER` | vier Zahlen | Einsatz x 9 |
| `LINE` | sechs Zahlen | Einsatz x 6 |
| `DOZEN` | zwölf Zahlen | Einsatz x 3 |
| `COLUMN` | zwölf Zahlen | Einsatz x 3 |
| `RED`, `BLACK`, `EVEN`, `ODD` | jeweils 18 Zahlen | Einsatz x 2 |

Bei `SPLIT` steht eine positive `betNumber` für den vertikalen Split `(n, n+3)`, eine negative Zahl
für den horizontalen Split `(|n|, |n|+1)`. Bei `STREET`, `LINE`, `DOZEN` und `COLUMN` beschreibt
`betNumber` die jeweilige Reihe, Doppelreihe, das Dutzend oder die Spalte. Für Farbe und Parität wird
der Wert ignoriert.

### Slotmachine

Die Slotmachine besitzt drei Walzen mit jeweils identischer Symbolverteilung:

| Symbol | Wahrscheinlichkeit pro Walze |
| --- | ---: |
| Cherry | 48,325 % |
| Lemon | 29,000 % |
| Bell | 21,000 % |
| Diamond | 1,675 % |

Die Wahrscheinlichkeiten werden beim Erstellen des Spinners validiert. Alle Symbole müssen vorhanden
sein, Einzelwerte müssen zwischen `0` und `1` liegen und die Summe muss innerhalb einer kleinen
Toleranz `1` ergeben.

| Kombination | Bruttoauszahlung |
| --- | ---: |
| Cherry x2 | Einsatz x 0,80 |
| Cherry x3 | Einsatz x 0,99 |
| Lemon x2 | Einsatz x 1,15 |
| Lemon x3 | Einsatz x 2,50 |
| Bell x2 | Einsatz x 1,70 |
| Bell x3 | Einsatz x 5,75 |
| Diamond x1 | Einsatz x 1,50 |
| Diamond x2 | Einsatz x 15,80 |
| Diamond x3 | Einsatz x 498,00 |

Treffen mehrere Regeln gleichzeitig zu, wird nur der höchste Multiplikator verwendet. Die aktuelle
Konfiguration zielt näherungsweise auf einen RTP von `97 %` und damit auf eine House Edge von `3 %`.

`won` bedeutet im Slot-Service bewusst **„eine Gewinnkombination wurde getroffen“** und nicht
zwangsläufig **„der Spieler hat netto Geld gewonnen“**. Besonders Cherry x2 und Cherry x3 zahlen
weniger als den vollständigen Einsatz zurück. Die Oberfläche kann dadurch einen Treffer anzeigen,
obwohl der Spieler tatsächlich einen Teil seines Einsatzes verloren hat. Genau dieser psychologische
Effekt war beabsichtigt: Der Spieler soll das Gefühl eines kleinen Erfolgs beziehungsweise eines
weniger schweren Verlusts bekommen, während die Statistik über `amount` weiterhin das echte
Nettoergebnis sichtbar macht.

## Architektur

Die Anwendung besteht aus drei Spring-Boot-Services und drei getrennten PostgreSQL-Datenbanken.
Roulette und Slotmachine besitzen keinen direkten Zugriff auf Bankdaten. Sie senden ausschließlich
das Nettoergebnis eines Spiels als Transaktion an die Bank.

Die PlantUML-Quellen befinden sich hier:

- [Gesamtsystem](docs/component-diagram.puml)
- [Bank-Service](bank/docs/component-diagram.puml)
- [Roulette-Service](roulette/docs/component-diagram.puml)
- [Slotmachine-Service](slotmachine/docs/component-diagram.puml)

Das Gesamtdiagramm zeigt alle sechs Container. Die internen Diagramme zeigen entsprechend der
Belegvorgabe nur den inneren Aufbau des jeweiligen Service und keine Datenbank-Container.

### Bank und Vertical Slices

Die Bank ist in die Slices `User` und `Transaction` gegliedert. Controller, Service, Model, View,
Repository, DTOs und Hilfsklassen werden möglichst der jeweiligen Subdomäne zugeordnet. Ein eigener
`Stat`-Slice wurde nicht umgesetzt, da dafür in den funktionalen Anforderungen keine Bank-Endpunkte
definiert waren und im Unterricht ausdrücklich besprochen wurde, diesen nicht zusätzlich zu bauen.

Die Transaction-Entity ist die einzige Quelle für Geldbewegungen. Ein User besitzt daher **_keinen
direkt veränderbaren Balance-Wert_**. Der Kontostand wird als Summe seiner Transaktionen berechnet.
Diese Entscheidung verhindert zwei konkurrierende Geldstände, führt innerhalb der geforderten
Slice-Trennung aber zu _zusätzlicher Kommunikation_.

Damit wurde vor allem das Problem gelöst, das ein deposit/withdraw über den User Endpunkt auch als eigene Transaction gilt. Der besprochene ungefähre Wortlaut war:

 _"Der Transaction-Slice 
ist damit die einzige source of truth
für Geldbeträge, der User-Slice speichert also keine eigene Balance sondern fragt Geldbeträge beim Transaction-Slice ab und summiert sich daraus die Balance. Damit stellt man auch sicher, das beide Slices sich gegenseitig brauchen, was bei Vertikal Slice wichtig ist."_

Ein Problem was dadurch leider auch auftritt ist, das bei jedem User Abruf die Balance "lazy" neu berechnet werden muss, selbst wenn schon große Mengen an Transaktionen vorliegen.

Der User-Slice ruft für Kontostände den Transaction-Slice über HTTP auf. Umgekehrt muss der
Transaction-Slice vor dem Erstellen einer Transaktion prüfen, ob der User existiert. Würde er dafür
`GET /user/{id}` aufrufen, würde dieser Endpunkt wieder den Kontostand aus dem Transaction-Slice
anfordern. Dadurch entstünde ein Aufrufkreis. Der zusätzliche Endpunkt `GET /user/{id}/exists`
liefert deshalb nur die Stammdaten und löst keine Balance-Berechnung aus. Dieser
Endpunkt wurde bei gemeinsamer Absprache mit dem Dozenten präsentiert und akzeptiert!

Das funktioniert und entspricht dem im Unterricht besprochenen Ansatz für getrennte Slices, ist aber
nicht optimal. Vor allem `GET /users` benötigt für die Kontostände mehrere interne Requests. Bei einer
Neuplanung würden wir User-Stammdaten, Konto und Transaktionsbuch klarer als eigene fachliche
Komponenten modellieren. Dann könnte beispielsweise ein Konto den aktuellen Saldo beziehungsweise
eine eigene Buchungslogik besitzen, während die User-Komponente nur Identität und Stammdaten hält.

### Ablauf eines Spiels

Roulette und Slotmachine folgen im Kern demselben Ablauf:

1. Request und Einsatz validieren.
2. Spielergebnis und Nettoergebnis berechnen.
3. Nettoergebnis als Banktransaktion senden.
4. Nur bei erfolgreicher Bankantwort das Spiel speichern.
5. Gespeichertes Ergebnis als View zurückgeben.

Damit wird bei einer abgelehnten Banktransaktion kein Spiel angelegt. Der Ablauf löst allerdings noch
keine verteilte Transaktion, was unter [Fehlende Atomarität](#fehlende-atomarität-zwischen-bank-und-spielservice)
genauer beschrieben wird.

## Tests

Die Tests sind als Unit-Tests aufgebaut. Eigene Fachlogik wird direkt geprüft, abstrakte Abhängigkeiten
wie Repositories, Clients, Factorys oder Spiellogik werden mit Mockito ersetzt. Es werden bewusst keine
Integrationstests gegen echte Datenbanken oder laufende Services ausgeführt.

Die Tests werden beim Bauen der Docker-Images automatisch ausgeführt. Lokal können sie mit Java 21
und Maven 3.9 auch einzeln gestartet werden:

```bash
mvn -f bank/pom.xml test
mvn -f roulette/pom.xml test
mvn -f slotmachine/pom.xml test
```

Der Schwerpunkt liegt auf eigenen Entities, Factorys, Services beziehungsweise Handlern, Controllern,
Mappern und Spielregeln. Warum nicht jede technische Klasse eine eigene Testklasse besitzt, wird im
Abschnitt [Testumfang](#testumfang) erläutert.

## Docker-Images für die Abgabe

Die fertigen Images sind wegen ihrer Dateigröße nicht als TAR-Archive im Repository oder in der
Moodle-Abgabe enthalten. Sie werden stattdessen über das öffentliche Docker-Hub-Repository
[timedelay/instantwin-enterprise](https://hub.docker.com/r/timedelay/instantwin-enterprise)
bereitgestellt.

Das Repository enthält drei getrennte Tags:

| Service | Docker-Image |
| --- | --- |
| Bank | `timedelay/instantwin-enterprise:bank` |
| Roulette | `timedelay/instantwin-enterprise:roulette` |
| Slotmachine | `timedelay/instantwin-enterprise:slotmachine` |

Die `compose.yml` verweist bereits auf diese Images. Zum Herunterladen und Starten der vollständigen
Anwendung inklusive der drei PostgreSQL-Datenbanken reichen daher folgende Befehle im Projektordner:

```bash
docker compose pull
docker compose up --no-build
```

Zum Beenden der Anwendung:

```bash
docker compose down
```

## Bewusste Abweichungen und technische Schulden

Während des Semesters hat sich nicht nur das Projekt, sondern auch unser Verständnis von Architektur
weiterentwickelt. Entsprechend wurde die Struktur mehrfach angepasst. Ab einem gewissen Stand musste
aber eine abgabefähige Lösung fertiggestellt werden, statt nach jeder neuen Erkenntnis große Teile neu
zu schreiben. Die folgenden Punkte sind uns deshalb bekannt und sollen nicht so wirken, als wären sie
übersehen worden.

### Pfade, Ports und JSON-Namen

Die APIs verwenden `/instantwin/...` statt `/casino/...`, weil wir der Anwendung mit
`InstantWin Enterprise` einen eigenen Namen gegeben haben. Die Services werden außerdem auf den
Ports `8081`, `8082` und `8083` veröffentlicht, damit alle drei gleichzeitig erreichbar sind.

Bei JSON wurde durchgehend `camelCase` statt der im Beleg gezeigten `snake_case`-Schreibweise
gewählt. Eine produktive API sollte solche Unterschiede bereits vor der Implementierung als festen
Vertrag definieren und versionieren. Für dieses Projekt ist die Abweichung dokumentiert und innerhalb
der Services konsistent.

### Deposit und Withdraw geben die Transaktion zurück

Die Deposit- und Withdraw-Endpunkte geben nicht den aktualisierten User mit Balance zurück, sondern
die Antwort der erstellten Transaktion als String. Fachlich sind beide Aktionen bei uns nur bequemere
Einstiegspunkte für eine Transaktion im User-Slice. Durch die interne HTTP-Kommunikation war das der
einfachste Weg, ohne anschließend einen weiteren Balance-Request auszulösen.

Sauberer wäre ein eigenes Response-Modell, das die erstellte Buchung und optional den danach
berechneten Kontostand enthält. Noch besser wäre die bereits beschriebene klarere Trennung zwischen
User-Stammdaten, Konto und Buchungen.

### Löschen von Usern und langfristige Identität

Der Beleg fordert kein Konzept dafür, wie die Daten der verschiedenen Slices beim Löschen eines
Users langfristig behandelt werden sollen. Deshalb wird aktuell nur der User im User-Slice gelöscht,
während seine bisherigen Transaktionen im Transaction-Slice erhalten bleiben. Ein Cascade-Delete
über die Slice-Grenze existiert nicht. Das verhindert zwar, dass die Buchungshistorie automatisch
verloren geht, die gespeicherten Transaktionen enthalten danach aber nur noch eine `userId`, die
keinem aktuellen User mehr eindeutig zugeordnet werden kann.

Die automatisch hochgezählten Datenbank-IDs sind für lokale Beziehungen innerhalb einer
Laufzeitumgebung ausreichend, sollten aber nicht gleichzeitig als dauerhafte, serviceübergreifende
fachliche Identität dienen. Solange die Sequenzen und Datenbanken unverändert bleiben, werden IDs
normalerweise nicht erneut vergeben. Bei unabhängigen Resets, Wiederherstellungen oder Migrationen
der User-, Transaction- oder Spieldatenbanken kann darauf jedoch nicht mehr zuverlässig vertraut
werden. Dann können Referenzen ins Leere zeigen, Historien unvollständig werden oder eine alte ID im
schlechtesten Fall einem neuen User zugeordnet werden.

Bei einer Neuplanung würden wir daher zwischen einer internen technischen ID und einer
unveränderlichen öffentlichen Konto- oder Kundennummer unterscheiden. Eine echte Kreditkartennummer
wäre wegen ihrer Sensibilität kein geeigneter allgemeiner Identifier; gemeint ist vielmehr ein
eigens erzeugter, nicht wiederverwendbarer Account-Identifier. Andere Services würden nur diesen
fachlichen Identifier kennen, während die `long`-ID auf den jeweiligen Service beziehungsweise die
lokale Datenbank beschränkt bliebe.

Auch dann sollten Transaktionen beim Löschen eines Users nicht zwangsläufig mitgelöscht werden, da
sie eine Buchungs- und Prüfhistorie darstellen. Sinnvoller wären beispielsweise ein Soft-Delete oder
ein geschlossener Account, die Pseudonymisierung persönlicher Userdaten und ein Ereignis wie
`UserDeleted`, auf das der Transaction-Slice reagieren kann. So bliebe die Historie über den stabilen
Account-Identifier nachvollziehbar, ohne einen bereits gelöschten User weiterhin als aktiv zu
behandeln. Dieser zusätzliche Identitäts- und Lebenszyklusmechanismus lag außerhalb der funktionalen
Anforderungen des Belegs, wäre für ein belastbares System aber notwendig.

### Negative Kontostände

Negative Kontostände sind im aktuellen Modell erlaubt. Weder Auszahlungen noch Spiele werden durch
einen Kreditrahmen oder eine Deckungsprüfung blockiert. Das wurde für das risikofreie virtuelle Spiel
bewusst akzeptiert. In einem echten Finanzsystem müsste die Konto-Domäne Regeln für verfügbares
Guthaben, Reservierungen, Limits und parallele Abbuchungen durchsetzen.

### User-Statistiken ohne gespeicherte Spiele

Roulette und Slotmachine antworten mit `404`, wenn zu einer User-ID keine Spiele vorhanden sind.
Dabei wird nicht unterschieden, ob der User in der Bank unbekannt ist oder lediglich noch nie in dem
betreffenden Service gespielt hat. Gemeint ist hier also „Spielerstatistik nicht gefunden“.

Eine zusätzliche Abfrage bei der Bank könnte beide Fälle unterscheiden, würde aber für einen
Statistik-Request einen weiteren synchronen HTTP-Aufruf erzeugen. Um die Services weniger eng zu
koppeln und unnötige Bankabfragen zu vermeiden, wurde darauf verzichtet.

### Fehlende Atomarität zwischen Bank und Spielservice

Eine Banktransaktion und das Speichern des zugehörigen Spiels bilden keine gemeinsame atomare
Transaktion. Zuerst wird die Bank aufgerufen, danach wird das Spiel lokal gespeichert. Scheitert das
Speichern nach einer erfolgreichen Bankantwort, existiert eine Geldbewegung ohne passenden
Spieldatensatz.

Für den Projektumfang wurde dieses Risiko akzeptiert. In einem belastbaren verteilten System würden
wir zum Beispiel eine Saga beziehungsweise einen Reservierungs- und Bestätigungsablauf verwenden:
Die Bank merkt eine Buchung zunächst mit einer eindeutigen Request-ID vor. Nach erfolgreichem
Speichern bestätigt der Spielservice die Buchung; bei einem Fehler wird sie verworfen oder durch eine
Kompensation zurückgebucht.

### Service-Verantwortlichkeiten und SRP

Mehrere Services beziehungsweise Handler besitzen aktuell mehr als einen Grund zur Änderung. Der
SlotGameService übernimmt zum Beispiel CRUD, Spielorchestrierung, Statistikberechnung und das Lesen
der Regeldateien. Der Roulette-Handler bündelt ebenfalls Spielablauf, Persistenz, Statistik und
Regeltexte. In der Bank liegen in den Slice-Services neben CRUD auch Orchestrierung und externe Calls.

Diese Aufteilung war für den überschaubaren Projektumfang handhabbar, verletzt aber teilweise das
Single-Responsibility-Prinzip. Bei einer Weiterentwicklung würden wir unter anderem getrennte
Game-Application-Services, Statistik-Services, Info-Provider und Persistenzadapter einführen. Dabei
sollte nicht für jede einzelne Methode eine neue Klasse entstehen; Ziel wäre eine Trennung nach echten
fachlichen Änderungsgründen.

### Erweiterbarkeit und OCP

Wettarten, Symbole, Wahrscheinlichkeiten und Auszahlungen sind überwiegend in Enums und Java-
Konfigurationsklassen fest codiert. Das war ein bewusster Kosten-Nutzen-Kompromiss, weil die im Beleg
benötigten Spielvarianten bekannt und zeitlich begrenzt waren. Neue Symbole, andere Walzengrößen oder
neue Roulette-Wetten erfordern dadurch jedoch Codeänderungen und einen neuen Build.

Eine flexiblere Lösung könnte Regeln über validierte `@ConfigurationProperties`, YAML-Dateien, eine
Datenbank oder eine kleine Administrationsoberfläche laden. Spielregeln könnten als Strategien
registriert werden, sodass neue Wettarten ergänzt werden, ohne bestehende Auswertungslogik zu ändern.
Für Slots wäre außerdem eine allgemeine Beschreibung von Walzenanzahl, sichtbaren Reihen,
Symbolmengen, Trefferlinien und Auszahlungsregeln sinnvoll. Das aktuelle 3x1-Modell war bewusst die
kleinste selbst entworfene Variante, könnte mit so einem Modell aber später zu 3x3, 5x3 oder weiteren
Symbolen erweitert werden. Hierfür bräuchte es für die benötigten Algorithmen, vor allem
wenn man die Spielgröße dynamisch halten möchte, noch eine klare Rechereche zu bereits vorhandenen Berechnungen oder implementierbaren Modellen. Da diese aber nur zweitrangig waren,
wurden die Kapazitäten hier auf die Recherche zu RTP (Return to player), Pay Tables und House Edge begrenzt.

Bei der Slot-Konfiguration wurde ein Teil des Interface-Segregation-Prinzips bereits berücksichtigt:
Spinner und Payout-Calculator erhalten mit `ISlotProbabilityConfiguration` und
`ISlotPayoutConfiguration` nur den für sie benötigten Ausschnitt. Die eigentlichen Werte bleiben aber
weiterhin statisch im Code.

### HTTP-Typen an der Client-Grenze

Die Bank-Clients der Spielservices geben `ResponseEntity<String>` zurück. Das ist praktisch, weil
Statuscode und Fehlertext ohne zusätzlichen Typ weitergereicht werden können. Gleichzeitig kennt
dadurch die Anwendungsschicht einen Spring-HTTP-Typ und ist stärker an das Transportprotokoll
gekoppelt, als es fachlich nötig wäre.

Ein besserer Adapter würde die HTTP-Antwort direkt in einen eigenen Typ wie `TransactionResult`
übersetzen. Dieser könnte zwischen Erfolg, unbekanntem User, fachlicher Ablehnung und technischem
Fehler unterscheiden. Der Service würde dann nur noch den fachlichen Typ kennen. Für diesen Beleg
wurde der zusätzliche Umbau nicht mehr vorgenommen; die Services selbst geben an ihre Controller
aber keine beliebigen `ResponseEntity`-Objekte zurück.

### Testumfang

Die Anforderung nach vollständig randomisierten Tests über volle Wertebereiche und Extremwerte wurde
mit dem Dozenten vor Ort besprochen. Für dieses Projekt wurde akzeptiert, überwiegend deterministische
Testwerte zu verwenden, weil der zusätzliche Aufwand nicht in einem guten Verhältnis zum Nutzen
stand. Zufällige Tests erschweren außerdem die Reproduzierbarkeit, wenn Seeds und erzeugte Fälle nicht
sauber protokolliert werden. Property-based Testing mit reproduzierbaren Seeds wäre bei einer
Weiterentwicklung die passendere Lösung.

Nicht jede Produktionsklasse besitzt eine eigene Testklasse. Reine DTOs, einfache Exceptions,
Framework-Konfigurationen und Clients ohne nennenswerte eigene Logik werden teilweise nur indirekt
über ihre Benutzer geprüft. Besonders bei den RestClients würde ein naiver Unit-Test größtenteils das
Verhalten von Spring testen. Eigene Fehlerübersetzung und Request-Erzeugung wären später sinnvolle
Kandidaten für fokussierte Client- oder Contract-Tests.

_Für Spring-Data-Repositories werden konkrete Entity-Typen verwendet._ Zusätzliche Entity-Interfaces
würden dort häufig nur vortäuschen, beliebige Implementierungen zu akzeptieren, während intern wieder
auf die konkrete JPA-Entity gecastet werden müsste. Auch dieser Verzicht wurde im Unterricht als
vertretbar besprochen. Wo eigene abstrakte Abhängigkeiten vorhanden sind, werden sie in Unit-Tests mit
Mockito ersetzt.

### Kommunikation zwischen den Services

Die größte rückblickende Erkenntnis ist, dass die Kommunikation zwischen Services viel früher und
genauer hätte geplant werden müssen. Wir haben über das Semester hinweg gelernt, wie sich
Microservices, Vertical Slices, REST-Ressourcen und Abhängigkeiten sinnvoll strukturieren lassen. Viele
dieser Erkenntnisse kamen aber erst, nachdem bereits Verträge und Datenmodelle implementiert waren.

Bei einem Neustart würden wir vor dem ersten Service einen gemeinsamen, versionierten
Ressourcenstandard für InstantWin definieren. Die Idee ähnelt dem Ansatz, den wir im Modul
„Medizinische Informationssysteme“ bei FHIR kennengelernt haben: Services tauschen nicht einfach
beliebige JSON-Objekte aus, sondern klar definierte Ressourcen mit gemeinsamer Semantik.

Eine kleine gemeinsame Contract-Library könnte beispielsweise enthalten:

- versionierte Kommunikations-Records und erlaubte Statuswerte,
- gemeinsame fachliche Werte wie `invoicingParty`, statt in jedem Service eigene Strings oder Enums zu führen,
- Bean-Validation-Regeln und Jackson-Mapper für ein einheitliches JSON-Format,
- einen standardisierten Fehlerkörper nach dem Prinzip eines `OperationOutcome`, der Fehlerart,
  betroffene Ressource, Feld, Ursache und verständliche Nachricht enthält,
- einen vorkonfigurierten Client beziehungsweise Mapper für neue Services.

Der erwartete Ressourcenstandard könnte zusätzlich über `Accept` und `Content-Type` verlangt werden,
zum Beispiel als eigener versionierter Media Type. Damit wäre früh sichtbar, welche Vertragsversion
ein Service senden und verstehen kann. Eine gemeinsame Library muss allerdings ebenfalls vorsichtig
versioniert werden, damit sie die Services nicht wieder zu einem gemeinsam auslieferbaren Monolithen
verklebt. Änderungen sollten daher rückwärtskompatibel, Ressourcen klein und die Veröffentlichung der
Library unabhängig von den Services sein.

So ein Ansatz hätte insbesondere die uneinheitlichen Kommunikationsobjekte, die String-Darstellung von
`invoicingParty` und die teilweise leeren oder nur textuellen Fehlerantworten verhindert. Vor allem
hätte er uns gezwungen, die Servicekommunikation als eigenen Architekturteil zu planen und nicht erst
beim Implementieren des nächsten HTTP-Calls festzulegen.

## Disclaimer
Zur Verbesserung der Codequalität und um fehlende Testabdekungen ausfindig zu machen wurde in diesem Projekt KI als Unterstüzung verwendet.

## Lizenz

Für dieses studentische Projekt ist aktuell keine separate Lizenzdatei hinterlegt. Die im Beleg
genannte Creative-Commons-Lizenz ist optional. Ohne eine ausdrücklich hinzugefügte Lizenz verbleiben
die Nutzungsrechte bei den Autoren.
