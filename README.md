# Runa

Runa ist eine virtuelle Kollegin für ein fiktives Restaurant. Bevor ein lokales Sprachmodell eine Anfrage sieht, ersetzt Runa personenbezogene Daten durch Decknamen wie `[KUNDE_1]`. Die Antwort wird danach lokal zurückübersetzt.

> **Status:** in Arbeit. Die Erkennung von E-Mail und IBAN ist fertig, der Rest folgt in den nächsten Tagen.

## Stack

Java 17, Maven, JUnit 5. Sprachmodell lokal über Ollama.

## Tests

```bash
mvn -q test
```

## Wie Runa entsteht

Ich baue Runa mit Claude Code. Ich plane jeden Schritt, lese jeden Diff, schreibe die wichtigsten Testfälle selbst und committe nur, was ich erklären kann.

## Lizenz

MIT
