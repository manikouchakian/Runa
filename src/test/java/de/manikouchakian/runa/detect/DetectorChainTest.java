package de.manikouchakian.runa.detect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class DetectorChainTest {

    // Feste Treffer, damit nur die Logik der Kette getestet wird und nicht die Muster.
    private static Match match(int start, int end) {
        return new Match(EntityType.PERSON, start, end, "x", "key-" + start);
    }

    private static Detector returning(Match... matches) {
        return text -> List.of(matches);
    }

    @Test
    void longerMatchWinsOnOverlap() {
        Match longer = match(0, 10);
        Match shorter = match(5, 8);
        DetectorChain chain = new DetectorChain(List.of(returning(shorter), returning(longer)));

        assertEquals(List.of(longer), chain.detect("egal"));
    }

    @Test
    void earlierMatchWinsOnOverlapWithSameLength() {
        Match earlier = match(0, 5);
        Match later = match(3, 8);
        DetectorChain chain = new DetectorChain(List.of(returning(later), returning(earlier)));

        assertEquals(List.of(earlier), chain.detect("egal"));
    }

    @Test
    void adjacentMatchesAreBothKept() {
        Match first = match(0, 5);
        Match second = match(5, 9);
        DetectorChain chain = new DetectorChain(List.of(returning(first), returning(second)));

        assertEquals(List.of(first, second), chain.detect("egal"));
    }

    @Test
    void resultIsSortedByStart() {
        Match first = match(0, 3);
        Match second = match(10, 12);
        Match third = match(20, 30);
        DetectorChain chain = new DetectorChain(List.of(returning(third, second), returning(first)));

        assertEquals(List.of(first, second, third), chain.detect("egal"));
    }

    @Test
    void emptyTextGivesEmptyList() {
        DetectorChain chain = new DetectorChain(
                List.of(new EmailDetector(), new IbanDetector(), new PhoneDetector()));

        assertTrue(chain.detect("").isEmpty());
    }

    @Test
    void ibanIsNotSplitIntoPhoneNumber() {
        // PhoneDetector allein findet in dieser IBAN "0044 0532 0130 00". Die Kette muss die IBAN behalten.
        DetectorChain chain = new DetectorChain(List.of(new IbanDetector(), new PhoneDetector()));

        List<Match> matches = chain.detect("DE89 3704 0044 0532 0130 00");

        assertEquals(1, matches.size());
        assertEquals(EntityType.IBAN, matches.get(0).type());
    }
}
