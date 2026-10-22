package de.manikouchakian.runa.detect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MatchTest {

    // Erfundene Adresse. Im Satz "Mail an Anna.Muster@Example.com" beginnt sie bei Index 8.
    private static final String TEXT = "Anna.Muster@Example.com";
    private static final String KEY = "anna.muster@example.com";

    @Test
    void validMatchKeepsAllValues() {
        Match match = new Match(EntityType.EMAIL, 8, 31, TEXT, KEY);

        assertEquals(EntityType.EMAIL, match.type());
        assertEquals(8, match.start());
        assertEquals(31, match.end());
        assertEquals(TEXT, match.text());
        assertEquals(KEY, match.key());
    }

    @Test
    void negativeStartIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Match(EntityType.EMAIL, -1, 22, TEXT, KEY));
    }

    @Test
    void startEqualToEndIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Match(EntityType.EMAIL, 8, 8, TEXT, KEY));
    }

    @Test
    void startGreaterThanEndIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Match(EntityType.EMAIL, 31, 8, TEXT, KEY));
    }

    @Test
    void nullTypeIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new Match(null, 8, 31, TEXT, KEY));
    }

    @Test
    void nullTextIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new Match(EntityType.EMAIL, 8, 31, null, KEY));
    }

    @Test
    void nullKeyIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new Match(EntityType.EMAIL, 8, 31, TEXT, null));
    }
}
