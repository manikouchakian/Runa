package de.manikouchakian.runa.detect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Findet E-Mail-Adressen über ein einfaches Muster.
 *
 * <p>Das Muster ist absichtlich einfach und nicht vollständig nach RFC 5322.
 * Bekannte Grenze: Adressen mit Umlauten in der Domain (zum Beispiel {@code müller.de})
 * werden nicht erkannt.
 */
public class EmailDetector implements Detector {

    // [A-Za-z0-9._%+-]+        lokaler Teil vor dem @, zum Beispiel "anna.muster"
    // @                        genau ein @
    // [A-Za-z0-9-]+            erster Teil der Domain, zum Beispiel "example"
    // (\.[A-Za-z0-9-]+)+       mindestens ein ".teil", zum Beispiel ".com"
    // Weil nach jedem Punkt ein Zeichen folgen muss, gehört ein Punkt am Satzende nicht dazu.
    // Einmal kompiliert, weil das Kompilieren teurer ist als das Suchen.
    private static final Pattern EMAIL =
            Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+");

    @Override
    public List<Match> detect(String text) {
        List<Match> matches = new ArrayList<>();
        Matcher matcher = EMAIL.matcher(text);
        while (matcher.find()) {
            String found = matcher.group();
            // Locale.ROOT, damit das Ergebnis nicht von der Spracheinstellung des Rechners abhängt
            String key = found.toLowerCase(Locale.ROOT);
            matches.add(new Match(EntityType.EMAIL, matcher.start(), matcher.end(), found, key));
        }
        // Unveränderliche Kopie, damit Aufrufer die Liste nicht nachträglich ändern
        return List.copyOf(matches);
    }
}
