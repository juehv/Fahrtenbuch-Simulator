/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.ztests;

import de.jhit.fbs.analyser.RouteAnalyzer;
import de.jhit.fbs.container.RawBook;
import de.jhit.fbs.container.Route;
import de.jhit.fbs.container.VisualizationEntry;
import de.jhit.fbs.generators.WayLengthRandomizer;
import de.jhit.fbs.csv.CsvInformationParser;
import de.jhit.fbs.csv.CsvSheetWriter;
import de.jhit.fbs.view.HtmlPrinter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

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
        RawBook book = CsvInformationParser.parseCsvFile("./test.csv");
        // read old static file for old answers
        List<Route> knownRoutes = CsvInformationParser.parseStaticFile("./static.csv");
        // analyse route
        List<Route> questionRoutes = RouteAnalyzer.returnSingleRoutesForQuestions(book, knownRoutes);
        // write new questions
        if (!CsvSheetWriter.writeQuestionSheet("./questions.csv", questionRoutes)) {
            JOptionPane.showMessageDialog(null, "Error while writing questions file");
            System.exit(-1);
        }
        // inform user
        JOptionPane.showMessageDialog(null, "Question Sheet generated. Please fill the table and klick ok.", "", JOptionPane.INFORMATION_MESSAGE);
        // read questions file
        questionRoutes = CsvInformationParser.parseAnsweredQuestionsFile("./questions.csv");
        // update route in book
        book = RouteAnalyzer.updateRoutes(book, knownRoutes, questionRoutes);
        // analyze fuel consumption
        
        // analyze waypoints (a->b b->c c->b ohne lücken)
        
        // reason analyzer (just for work: rf ohne hin fahrt, post ohne grund, kundenbesuch ohne grund ohne kunde --> schlagwörter analyse)
        
        // write suggestion book for review
        
        // inform user
        
        // read new book and restart (until no suggestions are nessesary)
        
        // visualize full information book and write down book and inform user for private/office/work kilometer
        // for write down book generate short term for routes with more than 3 entrys

        // step 2
        // read targets
        // check if target points fit (kundenrechnungen, parktickets, tankticket, werkstatt)
    }

    public static void testSimpleGenerator() throws Exception {
        // takes validation 
        // generates blocks with "insert x km here"
        // step 2
        // generate entrys before and after targets (fittet to 5 min)
        // generate post before customer visits
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
