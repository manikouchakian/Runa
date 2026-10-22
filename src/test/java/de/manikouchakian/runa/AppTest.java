package de.manikouchakian.runa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void mainPrintsStartMessage() {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        // System.out umleiten, damit wir die Ausgabe prüfen können
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            App.main(new String[0]);
        } finally {
            // Immer zurücksetzen, sonst sehen andere Tests keine Ausgabe mehr
            System.setOut(original);
        }
        assertEquals("Runa startet", buffer.toString(StandardCharsets.UTF_8).trim());
    }
}
