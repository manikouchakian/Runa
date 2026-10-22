package de.manikouchakian.runa.detect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Findet deutsche IBANs. Ein Muster findet Kandidaten, die Prüfsumme (ISO 13616, mod 97)
 * entscheidet, ob es wirklich eine IBAN ist.
 *
 * <p>Bewusste Entscheidung für den Start: nur deutsche IBANs ({@code DE} und 20 Ziffern).
 * Das Muster bleibt so einfach und erklärbar. Andere Länder haben andere Längen und
 * teilweise Buchstaben in der Mitte. Sie können später ergänzt werden.
 */
public class IbanDetector implements Detector {

    // \b              davor kein Buchstabe und keine Ziffer, also nicht mitten in "XDE89..."
    // DE              Ländercode, durch CASE_INSENSITIVE auch "de"
    // \d{2}           zwei Prüfziffern, zum Beispiel "89"
    // ( ?\d{4}){4}    vier Blöcke mit je vier Ziffern, Leerzeichen davor optional
    // ?\d{2}          die letzten zwei Ziffern
    // \b              danach keine weitere Ziffer, ein Punkt am Satzende ist aber erlaubt
    private static final Pattern IBAN =
            Pattern.compile("\\bDE\\d{2}( ?\\d{4}){4} ?\\d{2}\\b", Pattern.CASE_INSENSITIVE);

    @Override
    public List<Match> detect(String text) {
        List<Match> matches = new ArrayList<>();
        Matcher matcher = IBAN.matcher(text);
        while (matcher.find()) {
            String found = matcher.group();
            String key = found.replace(" ", "").toUpperCase(Locale.ROOT);
            // Das Muster prüft nur die Form. Erst die Prüfsumme trennt echte IBANs von Tippfehlern.
            if (hasValidChecksum(key)) {
                matches.add(new Match(EntityType.IBAN, matcher.start(), matcher.end(), found, key));
            }
        }
        return List.copyOf(matches);
    }

    /**
     * Prüft die IBAN nach ISO 13616.
     *
     * @param iban ohne Leerzeichen und in Großbuchstaben, zum Beispiel {@code DE89370400440532013000}
     * @return {@code true}, wenn der Rest der Division durch 97 genau 1 ist
     */
    private static boolean hasValidChecksum(String iban) {
        // Ländercode und Prüfziffern kommen ans Ende. So ist es in ISO 13616 festgelegt,
        // damit die Prüfziffern so gewählt werden können, dass am Ende Rest 1 herauskommt.
        String rearranged = iban.substring(4) + iban.substring(0, 4);
        int rest = 0;
        for (int i = 0; i < rearranged.length(); i++) {
            char c = rearranged.charAt(i);
            // Die Zahl hat 24 Stellen und passt nicht in long. Deshalb rechnen wir Stelle für
            // Stelle nur mit dem Rest weiter. Das Ergebnis ist dasselbe wie mit der ganzen Zahl.
            if (c >= 'A' && c <= 'Z') {
                // Buchstaben werden zu Zahlen: A=10 bis Z=35. Ab 10, damit sie sich nicht mit
                // den Ziffern 0 bis 9 überschneiden. Zwei Stellen, deshalb mal 100.
                int value = c - 'A' + 10;
                rest = (rest * 100 + value) % 97;
            } else {
                int digit = c - '0';
                rest = (rest * 10 + digit) % 97;
            }
        }
        return rest == 1;
    }
}
