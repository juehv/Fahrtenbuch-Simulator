/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.csv;

import com.csvreader.CsvReader;
import de.jhit.fbs.analyser.RouteAnalyzer;
import de.jhit.fbs.container.DataEntry;
import de.jhit.fbs.container.Location;
import de.jhit.fbs.container.RawBook;
import de.jhit.fbs.container.Route;
import de.jhit.fbs.smartcontainer.RouteTimeTable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jens
 */
public class CsvInformationParser {

    private static final String COMBINED_CSV_RAW_INPUT_HEADER = "Datum,Fahrzeit von,Fahrzeit bis,Startort,Umwege,Zielort,Zweck,Besuchte Person,gefahrene km,km-Stand (ende),Tanken (L),Typ,Privat,Dienst";
    private static final Pattern validEntry = Pattern.compile("\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d,.*,.*,.*,.*,.*,.*,.*,.*,.*,.*,.*,.*");

    public static RawBook parseCsvFile(String filepath)
            throws FileNotFoundException {
        RawBook book = new RawBook();
        book.entrys = new ArrayList<>();
        // read file
        CsvReader creader = new CsvReader(filepath, ',', Charset.forName("UTF-8"));

        try {
            // validate header
            creader.readHeaders();
            if (!valdiateHeader(creader)) {
                Logger.getLogger(CsvInformationParser.class.getName()).
                        log(Level.SEVERE, "Stop parser because of unvalid header:\n{0}"
                        + "\n valid header is:\n"
                        + COMBINED_CSV_RAW_INPUT_HEADER, creader.getRawRecord());
                return null;
            }

            // read entries
            while (creader.readRecord()) {
                DataEntry entry = parseEntry(creader);
                if (entry != null) {
                    book.entrys.add(entry);
                    System.out.println(creader.getCurrentRecord() + " " + entry.toString());
                } else {
                    Logger.getLogger(CsvInformationParser.class.getName()).
                            log(Level.WARNING, "Found unvalid entry: {0}",
                            creader.getRawRecord());
                }
            }


        } catch (IOException | ParseException ex) {
            Logger.getLogger(CsvInformationParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            creader.close();
        }
        return book;
    }

    private static boolean valdiateHeader(CsvReader reader) {
        // TODO implement
//        for (int i = 0; i < creader.getHeaderCount(); i++) {
//            System.out.println(creader.getHeader(i));
//        }
        return true;
    }

    private static DataEntry parseEntry(CsvReader reader)
            throws IOException, ParseException {
        Matcher entryMatcher = validEntry.matcher(reader.getRawRecord());
        if (entryMatcher.matches()) {
            DataEntry entry = new DataEntry();
            // get time
            String date = reader.get(0);
            String startT = reader.get(1);
            String endT = reader.get(2);
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
            entry.startTime = format.parse(date + " " + startT);
            entry.endTime = format.parse(date + " " + endT);
            if (entry.endTime.before(entry.startTime)) {
                // this drive is over midnight -> add 24 hours
                entry.endTime.setTime(entry.endTime.getTime() + 86400000);
            }
            // get route
            //TODO combine code for reading route
            Route route = new Route();
            route.start = new Location(reader.get(3));
            route.end = new Location(reader.get(5));
            route.km = Integer.parseInt(reader.get(8));

            route.detours = new ArrayList<>();
            String detoursString = reader.get(4);
            if (!detoursString.isEmpty()) {
                String[] detoursArray = detoursString.split(Route.DETOURS_DELIMITER);
                for (String item : detoursArray) {
                    if (!item.isEmpty()) {
                        route.detours.add(new Location(item));
                    }
                }
            }

            entry.route = route;
            // get time for route
            entry.route.typicalTimes = new RouteTimeTable();
            if (entry.startTime.getHours() >= 6
                    && entry.startTime.getHours() <= 9) {
                // Zone 1
                entry.route.typicalTimes.timeZone1 = calculateDurationForZone(
                        entry.startTime.getTime(),
                        entry.endTime.getTime());
            } else if (entry.startTime.getHours() >= 16
                    && entry.startTime.getHours() <= 18) {
                // Zone 2
                entry.route.typicalTimes.timeZone2 = calculateDurationForZone(
                        entry.startTime.getTime(),
                        entry.endTime.getTime());
            } else {
                // Zone 3
                entry.route.typicalTimes.timeZone3 = calculateDurationForZone(
                        entry.startTime.getTime(),
                        entry.endTime.getTime());
            }

            entry.reason = reader.get(6);
            entry.person = reader.get(7);
            entry.kmEnd = Integer.parseInt(reader.get(9));
            entry.kmStart = entry.kmEnd - Integer.parseInt(reader.get(8));
            switch (reader.get(11)) {
                case "p":
                case "P":
                    entry.type = DataEntry.TYPE_PRIVATE;
                    break;
                case "d":
                case "D":
                    entry.type = DataEntry.TYPE_WORK;
                    break;
                default:
                    Logger.getLogger(CsvInformationParser.class.getName())
                            .warning("Unknwon Type!" + reader.get(11));

            }
            String fuelString = reader.get(10);
            if (!fuelString.isEmpty()) {
                entry.fuelAmount = Double.parseDouble(fuelString.replaceAll(",", "."));
            }
//            entry.fuelMoney; //TODO implement
            return entry;
        }
        return null;
    }

    private static int calculateDurationForZone(long start, long end) {
        long duration = end - start;
        duration /= (1000 * 60);
        // fit to nearest 5
        duration = 5 * (Math.round(duration / 5));
        return (int) duration;
    }

    public static List<Route> parseStaticFile(String pathToStaticCsv) {
        // TODO implement
        return new ArrayList<>();
    }

    public static List<Route> parseAnsweredQuestionsFile(String questionsCsvPath)
            throws FileNotFoundException {
        List<Route> updatedRoutes = new ArrayList<>();
        // read file
        CsvReader creader = new CsvReader(questionsCsvPath, ',',
                Charset.forName("UTF-8"));

        try {
            // validate header
            // TODO find out how to skip comments -.- 
            creader.readHeaders();
            for (int i = 0; i < creader.getHeaderCount(); i++) {
                System.out.println(creader.getHeader(i));
            }
            // TODO check that structure is not modified

            // read entries
            while (creader.readRecord()) {
                Route entry = parseRoute(creader);
                if (entry != null) {
                    updatedRoutes.add(entry);
                    System.out.println(creader.getCurrentRecord() + " " + entry.toString());
                } else {
                    Logger.getLogger(CsvInformationParser.class.getName()).
                            log(Level.WARNING, "Found unvalid entry: {0}",
                            creader.getRawRecord());
                }
            }


        } catch (IOException /*| ParseException*/ ex) {
            Logger.getLogger(CsvInformationParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            creader.close();
        }
        return updatedRoutes;
    }

    private static Route parseRoute(CsvReader reader) throws IOException {
        if (reader.get(0).equalsIgnoreCase(RouteAnalyzer.DUPLICATE_MARKER)) {
            return null;
        }

        // TODO use regex to check format

        Route newEntry = new Route();

        newEntry.start = new Location(reader.get(1));
        newEntry.end = new Location(reader.get(2));

        newEntry.detours = new ArrayList<>();
        String detoursString = reader.get(3);
        if (!detoursString.isEmpty()) {
            String[] detoursArray = detoursString.split(Route.DETOURS_DELIMITER);
            for (String item : detoursArray) {
                if (!item.isEmpty()) {
                    newEntry.detours.add(new Location(item));
                }
            }
        }

        newEntry.km = Integer.parseInt(reader.get(4));

        RouteTimeTable times = new RouteTimeTable();
        times.timeZone1 = Integer.parseInt(reader.get(5));
        times.timeZone2 = Integer.parseInt(reader.get(6));
        times.timeZone3 = Integer.parseInt(reader.get(7));

        newEntry.typicalTimes = times;

        return newEntry;
    }
}
