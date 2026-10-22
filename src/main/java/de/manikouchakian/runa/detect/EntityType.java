package de.manikouchakian.runa.detect;

/**
 * Art eines erkannten Datums. Der Typ bestimmt später den Decknamen,
 * zum Beispiel wird aus {@code KUNDE} der Deckname {@code [KUNDE_1]}.
 */
public enum EntityType {
    KUNDE,
    LIEFERANT,
    PERSON,
    RECHNUNG,
    EMAIL,
    IBAN,
    TELEFON
}
