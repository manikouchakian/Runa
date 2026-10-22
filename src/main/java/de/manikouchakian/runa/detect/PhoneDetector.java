package de.manikouchakian.runa.detect;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Findet deutsche Telefonnummern. Ein Muster findet Kandidaten, danach müssen es
 * mindestens 7 Ziffern sein.
 *
 * <p>Bewusste Entscheidung für den ersten Release: nur deutsche Formate. Der {@code key}
 * wird auf {@code +49} vereinheitlicht, damit {@code 069 12345678} und
 * {@code +49 69 12345678} denselben Decknamen bekommen.
 *
 * <p>Bekannte Grenzen:
 * <ul>
 *   <li>{@code -} ist kein Trennzeichen, sonst würde zum Beispiel {@code 0815-2026} erkannt.</li>
 *   <li>{@code +49 (0)69 ...} wird nicht erkannt.</li>
 *   <li>Folgt eine Zahl mit Leerzeichen, gehört sie zum Treffer ({@code 069 12345678 20 Uhr}).
 *       Dann wird etwas mehr ersetzt als nötig, aber nichts verraten.</li>
 * </ul>
 */
public class PhoneDetector implements Detector {

    private static final int MIN_DIGITS = 7;

    // (\+49 ?|\b0)    Anfang: "+49" mit optionalem Leerzeichen, oder eine 0,
    //                 vor der kein Buchstabe und keine Ziffer steht
    // \d+             Ziffern, zum Beispiel "69" oder "171"
    // ([ /]\d+)*      beliebig viele weitere Ziffernblöcke, getrennt durch Leerzeichen oder /
    private static final Pattern PHONE = Pattern.compile("(\\+49 ?|\\b0)\\d+([ /]\\d+)*");

    @Override
    public List<Match> detect(String text) {
        List<Match> matches = new ArrayList<>();
        Matcher matcher = PHONE.matcher(text);
        while (matcher.find()) {
            String found = matcher.group();
            // Das Muster allein findet auch "03" aus "03.10.2026". Erst die Länge trennt
            // Telefonnummern von Datum, Postleitzahl und Nummernteilen.
            if (countDigits(found) >= MIN_DIGITS) {
                matches.add(new Match(EntityType.TELEFON, matcher.start(), matcher.end(),
                        found, normalize(found)));
            }
        }
        return List.copyOf(matches);
    }

    private static int countDigits(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    /** Macht aus "069 12345678" und "+49 69 12345678" denselben Schlüssel "+496912345678". */
    private static String normalize(String found) {
        String compact = found.replace(" ", "").replace("/", "");
        if (compact.startsWith("0")) {
            // Die führende 0 ist die deutsche Vorwahl-Null, international steht dafür +49
            return "+49" + compact.substring(1);
        }
        return compact;
    }
}
