package de.manikouchakian.runa.detect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class EmailDetectorTest {

    private final EmailDetector detector = new EmailDetector();

    @Test
    void findsAddressWithPosition() {
        List<Match> matches = detector.detect("Mail an anna.muster@example.com bitte");

        assertEquals(1, matches.size());
        Match match = matches.get(0);
        assertEquals(EntityType.EMAIL, match.type());
        assertEquals(8, match.start());
        assertEquals(31, match.end());
        assertEquals("anna.muster@example.com", match.text());
    }

    @Test
    void uppercaseAddressGivesLowercaseKey() {
        Match match = detector.detect("Mail an Anna.Muster@Example.com").get(0);

        assertEquals("Anna.Muster@Example.com", match.text());
        assertEquals("anna.muster@example.com", match.key());
    }

    @Test
    void dotAtEndOfSentenceIsNotPartOfAddress() {
        Match match = detector.detect("Schreib an anna.muster@example.com.").get(0);

        assertEquals(34, match.end());
        assertEquals("anna.muster@example.com", match.text());
    }

    @Test
    void twoAddressesGiveTwoMatchesInOrder() {
        List<Match> matches = detector.detect("anna@example.com und ben@example.org");

        assertEquals(2, matches.size());
        assertEquals(0, matches.get(0).start());
        assertEquals("anna@example.com", matches.get(0).text());
        assertEquals(21, matches.get(1).start());
        assertEquals("ben@example.org", matches.get(1).text());
    }

    @Test
    void textWithoutAddressGivesEmptyList() {
        assertTrue(detector.detect("Tisch für vier um 19 Uhr").isEmpty());
    }

    @Test
    void atSignAloneGivesEmptyList() {
        assertTrue(detector.detect("@").isEmpty());
    }

    @Test
    void domainWithoutDotGivesEmptyList() {
        assertTrue(detector.detect("a@b").isEmpty());
    }
}
