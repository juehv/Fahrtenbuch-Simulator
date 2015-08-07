/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.ztests;

import de.jhit.fbs.container.VisualizationEntry;
import de.jhit.fbs.generators.WayLengthRandomizer;
import de.jhit.fbs.view.HtmlPrinter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jens
 */
public class Task {

    public static void testVisualization() throws Exception {

        List<VisualizationEntry> entrys = new ArrayList<>();
        entrys.add(new VisualizationEntry());
        entrys.add(new VisualizationEntry());
        entrys.add(new VisualizationEntry());
        entrys.add(new VisualizationEntry());

        new HtmlPrinter().print(entrys);
        
        //TODO add visualization to write down by hand (no private times or routes, use handwritten font)
    }

    public static void testNormalOperation() throws Exception {

        // csv mit targets einlesen
        // csv mit echten fahrten einlesen (optional)
        // csv generieren mit entfernungsfragen, tank placement fragen
        // placen bis letzte werkstadtrouter
        // privatanteil anzeigen und fake dienstfahrten/office fahrten vorschlagen (nicht zu viele office fahrten), ersatzteil fahrten /achtung bei parktickets -> bestätigungs gui
        // nach bestätigung placen
        // drucken
    }

    public static void testValidation() throws Exception {
        // read csv fahrtenbuch
        // convert to internel representation
        // generate lengh asker (gui bitte eintragen und csv generieren und pfad angeben)
        // read answers
        // validate ways

        // step 2
        // read targets
        // check if target points fit (kundenrechnungen, parktickets, tankticket, werkstatt)
        // check fuel consumption
    }

    public static void testSimpleGenerator() throws Exception {
        // takes validation 
        // generates blocks with "insert x km here"
        
        
        // step 2
        // generate entrys before and after targets (fittet to 5 min)
    }

    public static void testRandomizer() {
        for (int i = 0; i < 100; i++) {
            System.out.println(WayLengthRandomizer.getRandomDeviation());
        }

        for (int i = 0; i < 10; i++) {
            int counter = 0;
            for (int j = 0; j < 10000; j++) {
                int rand = WayLengthRandomizer.getRandomDeviation();
                if (rand == -1) {
                    counter++;
                }
            }
            System.out.println("-1 Anteil: " + counter / 100 + "%");
        }
    }
}
