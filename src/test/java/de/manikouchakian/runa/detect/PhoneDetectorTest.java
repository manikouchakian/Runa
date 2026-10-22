package de.manikouchakian.runa.detect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PhoneDetectorTest {

    private final PhoneDetector detector = new PhoneDetector();

    // Leere rechte Spalte bedeutet: es darf nichts erkannt werden.
    // Alle Nummern sind erfunden.
    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', textBlock = """
            +49 69 12345678 | +496912345678
            069 12345678    | +496912345678
            069/1234567     | +49691234567
            0171 2345678    | +491712345678
            03.10.2026      |
            412,30          |
            R-2026-0815     |
            60311           |
            KD-10023        |
            """)
    void detectsGermanPhoneNumbers(String input, String expectedKey) {
        List<Match> matches = detector.detect(input);

        if (expectedKey == null) {
            assertTrue(matches.isEmpty());
        } else {
            assertEquals(1, matches.size());
            assertEquals(EntityType.TELEFON, matches.get(0).type());
            assertEquals(expectedKey, matches.get(0).key());
        }
    }

    @Test
    void phoneInSentenceHasCorrectPosition() {
        Match match = detector.detect("Ruf an unter 069 12345678 bitte").get(0);

        assertEquals(13, match.start());
        assertEquals(25, match.end());
        assertEquals("069 12345678", match.text());
    }
}
