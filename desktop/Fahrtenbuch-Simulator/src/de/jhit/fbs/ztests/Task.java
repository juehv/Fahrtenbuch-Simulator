/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.ztests;

import de.jhit.fbs.analyser.FuelAnalyzer;
import de.jhit.fbs.analyser.RouteAnalyzer;
import de.jhit.fbs.analyser.TargetAnalyzer;
import de.jhit.fbs.container.RawBook;
import de.jhit.fbs.container.Route;
import de.jhit.fbs.container.VisualizationEntry;
import de.jhit.fbs.generators.WayLengthRandomizer;
import de.jhit.fbs.csv.CsvInformationParser;
import de.jhit.fbs.csv.CsvSheetWriter;
import de.jhit.fbs.view.HtmlPrinter;
import de.jhit.fbs.view.PrintableEntryFactory;
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

        new HtmlPrinter().printFullBook(entrys, "./tmp.html");

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
        RawBook book = new RawBook();
        book.entrys = CsvInformationParser.parseCsvBookEntrys("./test.csv", false);
        if (book.entrys == null) {
            // Parser exits with error
            JOptionPane.showMessageDialog(null, "Error while reading file");
            System.exit(-1);
        }
        // read targets
//        book.targets = CsvInformationParser.parseCsvTargetEntrys("./targets.csv");
//        if (book.targets == null) {
//            // Parser exits with error
//            JOptionPane.showMessageDialog(null, "Error while reading file");
//            System.exit(-1);
//        }
//        // create missing entrys from targets
//        book = TargetAnalyzer.matchTargetsToBook(book);
//        if (!CsvSheetWriter.writeSuggestedBook("./target_book.csv", book)) {
//            JOptionPane.showMessageDialog(null, "Error while writing target book file",
//                    "", JOptionPane.ERROR_MESSAGE);
//            System.exit(-1);
//        }
//        // inform user
//        JOptionPane.showMessageDialog(null, "I matched the targets.\n"
//                + "Please review the book and click ok if done.", "",
//                JOptionPane.INFORMATION_MESSAGE);
//        // read new book
//        book.entrys = CsvInformationParser.parseCsvBookEntrys("./suggestion.csv", true);
//        if (book.entrys == null) {
//            // Parser exits with error
//            JOptionPane.showMessageDialog(null, "Error while reading file", "",
//                    JOptionPane.ERROR_MESSAGE);
//            System.exit(-1);
//        }
//        
        // read old static file for old answers
        List<Route> knownRoutes = CsvInformationParser.parseStaticFile("./static.csv");
        // analyse route
        List<Route> questionRoutes = RouteAnalyzer.returnSingleRoutesForQuestions(book.getRoutes(), knownRoutes);
        // write new questions
        if (!CsvSheetWriter.writeQuestionSheet("./questions.csv", questionRoutes)) {
            JOptionPane.showMessageDialog(null, "Error while writing questions file", "", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        // inform user
        JOptionPane.showMessageDialog(null, "Question Sheet generated. Please fill the table and klick ok.", "", JOptionPane.INFORMATION_MESSAGE);
        // read questions file
        questionRoutes = CsvInformationParser.parseAnsweredQuestionsFile("./questions.csv");
        knownRoutes.addAll(questionRoutes);
        // update known routes
        if (!CsvSheetWriter.writeNewStaticFile("./static.csv", knownRoutes)) {
            JOptionPane.showMessageDialog(null, "Error while writing db", "",
                    JOptionPane.WARNING_MESSAGE);
        }
        // update route in book
        book = RouteAnalyzer.updateRoutes(book, knownRoutes);
        // check if km counter is correct
        book = RouteAnalyzer.checkKilometerCounter(book);
        // check if drive times are correct. add warning marker if not.
        book = RouteAnalyzer.checkRouteTimes(book);
        // check if km are correct. add warning marker if not.
        book = RouteAnalyzer.checkRouteDistance(book, knownRoutes);
        // check if wayoints are without gabs. add warning marker if not.
        book = RouteAnalyzer.checkRouteWaypoints(book);
        // analyze fuel consumption
        book = FuelAnalyzer.calculateFuelConsumption(book);
        book = FuelAnalyzer.calculateMissingInformation(book, 5, 8);
        // write suggestion book for review
        if (!CsvSheetWriter.writeSuggestedBook("./suggestion.csv", book)) {
            JOptionPane.showMessageDialog(null, "Error while writing suggestion file",
                    "", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        // inform user
        JOptionPane.showMessageDialog(null, "I think I know what you mean.\n"
                + "Please review the book and click ok if done.", "",
                JOptionPane.INFORMATION_MESSAGE);
        // read new book
        book.entrys = CsvInformationParser.parseCsvBookEntrys("./suggestion.csv", true);
        if (book.entrys == null) {
            // Parser exits with error
            JOptionPane.showMessageDialog(null, "Error while reading file", "",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        // ask for recheck if yes restart
        int result = JOptionPane.showConfirmDialog(null,
                "Processing done. Recheck everything?", "", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            //TODO restart
        }// else continue
        // analyse for short terms (more than 6 time a waypoint)
        book.shortcuts = RouteAnalyzer.generateRouteShortcuts(book.getRoutes());
        // visualize full information book
        new HtmlPrinter().printFullBook(PrintableEntryFactory.convertEntrys(book.entrys), "./tmp.html");
        // visualize write down book and shortcut table
//        new HtmlPrinter().printHandwriteBook(PrintableEntryFactory.convertEntrys(book.entrys), book.shortcuts);

        // visualize full information book and write down book and inform user for private/office/work kilometer
        // for write down book generate short term for routes with more than 3 entrys
        // step 2
        // read targets
        // check if target points fit (kundenrechnungen, parktickets, tankticket, werkstatt)
        // step 3
        // reason analyzer (just for work: rf ohne hin fahrt, post ohne grund, kundenbesuch ohne grund ohne kunde --> schlagwörter analyse)
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
