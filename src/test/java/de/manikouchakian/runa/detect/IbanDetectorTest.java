package de.manikouchakian.runa.detect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IbanDetectorTest {

    private final IbanDetector detector = new IbanDetector();

    // Leere rechte Spalte bedeutet: es darf nichts erkannt werden.
    // DE89 3704 0044 0532 0130 00 ist die bekannte Beispiel-IBAN aus der Dokumentation, kein echtes Konto.
    @ParameterizedTest(name = "{0} -> {1}")
    @CsvSource(delimiter = '|', textBlock = """
            DE89 3704 0044 0532 0130 00           | DE89370400440532013000
            DE89370400440532013000                | DE89370400440532013000
            DE88 3704 0044 0532 0130 00           |
            Bitte an DE89 3704 0044 0532 0130 00. | DE89370400440532013000
            de89 3704 0044 0532 0130 00           | DE89370400440532013000
            DE89 3704 0044 0532 0130              |
            DE89 3704 0044 X532 0130 00           |
            """)
    void detectsOnlyValidGermanIbans(String input, String expectedKey) {
        List<Match> matches = detector.detect(input);

        if (expectedKey == null) {
            assertTrue(matches.isEmpty());
        } else {
            assertEquals(1, matches.size());
            assertEquals(EntityType.IBAN, matches.get(0).type());
            assertEquals(expectedKey, matches.get(0).key());
        }
    }

    @Test
    void ibanInSentenceExcludesTrailingDot() {
        Match match = detector.detect("Bitte an DE89 3704 0044 0532 0130 00.").get(0);

        assertEquals(9, match.start());
        assertEquals(36, match.end());
        assertEquals("DE89 3704 0044 0532 0130 00", match.text());
    }
}
