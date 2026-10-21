# Runa

Runa ist eine virtuelle Kollegin für ein Restaurant. Mitarbeitende schreiben ihr Anfragen
in normaler Sprache. Bevor ein Sprachmodell den Text sieht, ersetzt Runa alle
personenbezogenen Daten durch Decknamen wie `[KUNDE_1]`. Die Antwort des Modells wird
danach lokal zurückübersetzt.

Dieses Projekt ist ein Lernprojekt. Mani muss jede Zeile erklären können.

## Stack

* Java 17, Maven, JUnit 5
* Javalin (Web), Jackson (JSON), sqlite-jdbc (Datenbank), slf4j-simple (Logging für Javalin)
* LLM lokal über Ollama (HTTP mit `java.net.http`)
* Keine weiteren Abhängigkeiten ohne Rückfrage. Kein Spring, kein LangChain4j, keine Regex-Bibliotheken.

## Pakete (`de.manikouchakian.runa`)

* `detect`: Erkennung. `EntityType`, `Match`, `Detector`, `IbanDetector`, `EmailDetector`, `PhoneDetector`, `DatabaseDetector`, `DetectorChain`
* `db`: `Database` (Verbindung, Schema, Seed), `EntityRepository`, `KnownEntity`
* `vault`: `Vault` (Decknamen einer Sitzung), `Pseudonymizer`, `Reidentifier`
* `llm`: `LlmClient` (Interface), `OllamaClient`, `FakeLlm`
* `core`: `Runa` (Ablauf), `RunaResult`
* `web`: `WebServer` (Javalin)
* `App`: Startpunkt, liest Umgebungsvariablen

## Unverhandelbare Regeln

1. IMPORTANT: Kein Text erreicht `LlmClient`, bevor er durch `Pseudonymizer` gelaufen ist. Nur `Runa` ruft `LlmClient` auf.
2. Echte Kennungen (Kundennummern, Rechnungsnummern, Namen, E-Mail, IBAN, Telefon) verlassen das System nie. Decknamen werden pro Sitzung neu vergeben und sind nie aus echten Daten abgeleitet.
3. Tests nutzen nie das Netzwerk und nie ein echtes Ollama. Nur `FakeLlm` und SQLite in-memory (`jdbc:sqlite::memory:`).
4. Bestehende Tests niemals ändern, damit sie grün werden. Wenn ein Test falsch wirkt: stoppen, Grund erklären, fragen.
5. Alle Testdaten sind erfunden. Keine echten Firmen, Personen oder Kontodaten.
6. Geldbeträge als `BigDecimal`, in der Datenbank als `INTEGER` in Cent. Nie `double`.

## Arbeitsweise

* Immer zuerst einen Plan zeigen, dann auf Freigabe warten.
* Ein Schritt ist klein: eine Änderung, passende Tests, fertig. Nicht committen, Mani committet selbst.
* Nur Dateien anlegen oder ändern, die im Auftrag genannt sind.
* Nach jeder Änderung `mvn -q test` ausführen und das Ergebnis zeigen.
* Lesbar vor clever. Klassen möglichst unter 150 Zeilen. Keine Streams, wenn eine Schleife klarer ist.
* Javadoc und Kommentare erklären das Warum, nicht das Was. Kommentare auf Deutsch, Code und Bezeichner auf Englisch.
* Bei Fehlern: erst die Ursache erklären, dann einen Vorschlag machen, dann warten.
* Wenn Mani fragt: Zeile für Zeile erklären, mit konkreten Beispielwerten.

## Befehle

* Tests: `mvn -q test`
* Starten mit Fake-Modell: `RUNA_LLM=fake mvn -q compile exec:java`
* Starten mit Ollama: `ollama serve` und dann `mvn -q compile exec:java`

## Umgebungsvariablen

* `RUNA_LLM`: `ollama` (Standard) oder `fake`
* `RUNA_MODEL`: Standard `qwen2.5:7b`
* `RUNA_DB`: Pfad zur SQLite-Datei, Standard `runa.db`

## Begriffe

* Deckname (Token): Platzhalter wie `[KUNDE_1]`, gilt nur in einer Sitzung
* Sitzung: eine Anfrage an `/api/ask`. Pro Sitzung ein neuer `Vault`
* Fundstelle (`Match`): Typ, Start, Ende und Text eines erkannten Datums
* Anzeigewert: was bei der Rückübersetzung eingesetzt wird, zum Beispiel `Frau Muster`
