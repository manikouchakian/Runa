---
description: Schlägt einen Kaputtmach Test vor, um zu prüfen, ob die Tests eine Klasse wirklich absichern
argument-hint: [datei]
disable-model-invocation: true
---

Kaputtmach Test für $ARGUMENTS. Ändere selbst KEINE Datei.

1. Nenne die eine Zeile in $ARGUMENTS, deren Fehler am gefährlichsten wäre, und warum.
2. Beschreibe eine kleine, realistische Änderung an dieser Zeile (ein typischer Programmierfehler).
3. Frage Mani: Welcher Test wird deiner Meinung nach rot? Warte auf die Antwort.
4. Nach Manis Antwort: Mani ändert die Zeile selbst und führt `mvn -q test` aus. Werte das Ergebnis aus.
5. Wenn kein Test rot wird: Beschreibe den fehlenden Testfall, schreibe ihn aber erst nach Freigabe.
6. Erinnere Mani daran, die Änderung danach zurückzusetzen.
