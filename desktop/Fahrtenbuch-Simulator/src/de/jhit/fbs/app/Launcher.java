/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.app;

import de.jhit.fbs.container.Entry;
import de.jhit.fbs.view.HtmlPrinter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jens
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        List<Entry> entrys = new ArrayList<>();
        entrys.add(new Entry());
        entrys.add(new Entry());
        entrys.add(new Entry());
        entrys.add(new Entry());
        
       new HtmlPrinter().print(entrys);
       
       
       // csv mit targets einlesen
       // csv mit echten fahrten einlesen (optional)
       // csv generieren mit entfernungsfragen, tank placement fragen
       // placen bis letzte werkstadtrouter
       // privatanteil anzeigen und fake dienstfahrten/office fahrten vorschlagen (nicht zu viele office fahrten), ersatzteil fahrten /achtung bei parktickets -> bestätigungs gui
       // nach bestätigung placen
       // drucken
       
    }
}
