package de.manikouchakian.runa.detect;

import java.util.Objects;

/**
 * Eine Fundstelle im Text.
 *
 * <p>{@code start} ist inklusiv, {@code end} exklusiv, wie bei {@link String#substring(int, int)}.
 * {@code key} identifiziert das Objekt eindeutig. So bekommt dieselbe Person denselben
 * Decknamen, auch wenn sie im Text unterschiedlich geschrieben ist. Bei E-Mail, IBAN und
 * Telefon ist {@code key} der normalisierte Text.
 */
public record Match(EntityType type, int start, int end, String text, String key) {

    /** Prüft die Werte sofort, damit ein kaputter Match nicht erst beim Ersetzen auffällt. */
    public Match {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(text, "text must not be null");
        Objects.requireNonNull(key, "key must not be null");
        if (start < 0) {
            throw new IllegalArgumentException("start must not be negative: start=" + start);
        }
        if (start >= end) {
            throw new IllegalArgumentException(
                    "start must be less than end: start=" + start + ", end=" + end);
        }
    }
}
