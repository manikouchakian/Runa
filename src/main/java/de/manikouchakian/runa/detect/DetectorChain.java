package de.manikouchakian.runa.detect;

import java.util.ArrayList;
import java.util.List;

/**
 * Ruft mehrere Detektoren auf und liefert eine Liste ohne Überlappungen, sortiert nach Start.
 *
 * <p>Überlappungen kommen wirklich vor: Der {@link PhoneDetector} findet in einer IBAN wie
 * {@code DE89 3704 0044 0532 0130 00} den Teil {@code 0044 0532 0130 00}. Ohne diese Kette
 * würde später ein Teil der IBAN als Telefonnummer ersetzt.
 *
 * <p>Regel bei Überlappung: der längere Treffer gewinnt, bei gleicher Länge der frühere.
 * Haben zwei Treffer gleichen Start und gleiche Länge, bleibt der vom Detektor, der in der
 * Liste weiter vorne steht.
 */
public class DetectorChain implements Detector {

    private final List<Detector> detectors;

    public DetectorChain(List<Detector> detectors) {
        // Kopie, damit eine spätere Änderung an der übergebenen Liste die Kette nicht verändert
        this.detectors = List.copyOf(detectors);
    }

    @Override
    public List<Match> detect(String text) {
        List<Match> all = new ArrayList<>();
        for (Detector detector : detectors) {
            all.addAll(detector.detect(text));
        }

        // Wichtigste Treffer zuerst. So muss beim Durchgehen nie ein schon behaltener
        // Treffer wieder entfernt werden.
        all.sort(DetectorChain::compareByPriority);

        List<Match> kept = new ArrayList<>();
        for (Match candidate : all) {
            if (!overlapsAny(candidate, kept)) {
                kept.add(candidate);
            }
        }

        kept.sort((a, b) -> Integer.compare(a.start(), b.start()));
        return List.copyOf(kept);
    }

    /** Längere Treffer zuerst, bei gleicher Länge der mit dem kleineren Start. */
    private static int compareByPriority(Match a, Match b) {
        // b vor a, damit die größere Länge nach vorne kommt
        int byLength = Integer.compare(length(b), length(a));
        if (byLength != 0) {
            return byLength;
        }
        return Integer.compare(a.start(), b.start());
    }

    private static int length(Match match) {
        return match.end() - match.start();
    }

    private static boolean overlapsAny(Match candidate, List<Match> kept) {
        for (Match other : kept) {
            // end ist exklusiv. Bei end == start berühren sich die Treffer nur und überlappen nicht.
            if (candidate.start() < other.end() && other.start() < candidate.end()) {
                return true;
            }
        }
        return false;
    }
}
