package de.manikouchakian.runa.detect;

import java.util.List;

/**
 * Findet eine Art von Daten in einem Text. Jede Erkennung ist eine eigene Klasse,
 * damit man sie einzeln testen und in der {@code DetectorChain} kombinieren kann.
 */
public interface Detector {

    /**
     * Sucht im Text nach Fundstellen.
     *
     * @param text der Text der Anfrage
     * @return alle Fundstellen, leere Liste wenn nichts gefunden wurde, nie {@code null}
     */
    List<Match> detect(String text);
}
