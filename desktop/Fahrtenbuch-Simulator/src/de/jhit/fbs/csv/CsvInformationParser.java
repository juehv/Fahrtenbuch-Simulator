/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.csv;

import com.csvreader.CsvReader;
import de.jhit.fbs.container.DataEntry;
import de.jhit.fbs.container.Location;
import de.jhit.fbs.container.Constants;
import de.jhit.fbs.container.Marker;
import de.jhit.fbs.container.Route;
import de.jhit.fbs.smartcontainer.RouteTimeTable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class CsvInformationParser {
    // TODO find common format and rewrite code
    // init book = finish book format
    // static file = question file
    // use logger for warnings
    // write funktion to generate empty table
    // use header names instead of in to enable flexible tables with and without marker coloumn

    //TODO move to util class
    public static int calculateDurationForZone(long start, long end) {
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
                Route entry = parseQuestionRoute(creader);
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

    private static Route parseQuestionRoute(CsvReader reader) throws IOException {
        String[] markers = reader.get(0).split(Constants.MARKER_DELIMITER);
        if (markers != null && markers.length > 0
                && Arrays.asList(markers).contains(Constants.DUPLICATE_MARKER)) {
            return null;
        }

        // TODO use regex to check format
        Route newEntry = new Route();

        newEntry.start = new Location(reader.get(1));
        newEntry.end = new Location(reader.get(2));

        newEntry.detours = new ArrayList<>();
        String detoursString = reader.get(3);
        if (!detoursString.isEmpty()) {
            String[] detoursArray = detoursString.split(Constants.DETOURS_DELIMITER);
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

    /**
     *
     * @param filepath
     * @param isExtendedFormat
     * @return
     * @throws FileNotFoundException
     */
    public static List<DataEntry> parseCsvBookEntrys(String filepath, boolean isExtendedFormat)
            throws FileNotFoundException {
        List<DataEntry> bookEntrys = new ArrayList<>();
        // read file
        CsvReader creader = new CsvReader(filepath, ',', Charset.forName("UTF-8"));

        try {
            // validate header
            creader.readHeaders();
            if (!CsvValidator.validateSimpleBookHeader(creader)) {
                Logger.getLogger(CsvInformationParser.class.getName()).
                        log(Level.SEVERE,
                                "Stop parser because of unvalid header:\n{0}",
                                creader.getRawRecord());
                return null;
            }
            if (isExtendedFormat) {
                if (!CsvValidator.validateBookHeaderExtentions(creader)) {
                    Logger.getLogger(CsvInformationParser.class.getName()).
                            log(Level.SEVERE,
                                    "Stop parser because of missing extended header fields:\n{0}",
                                    creader.getRawRecord());
                    return null;
                }
            }

            // read entries
            while (creader.readRecord()) {
                // check if entry is valid
                if (CsvValidator.validateCsvEntry(creader.getRawRecord())) {
                    DataEntry entry = new DataEntry();
                    if (isExtendedFormat) {
                        entry = parseExtendedFields(entry, creader);
                        if (!CsvValidator.isMarkerValid(entry.marker)) {
                            // skip parsing if marker says its an unvalid entry
                            continue;
                        }
                    }
                    entry = parseEntry(entry, creader);
                    bookEntrys.add(entry);
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
        return bookEntrys;
    }

    /**
     *
     * @param reader
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private static DataEntry parseEntry(DataEntry entry, CsvReader reader)
            throws IOException, ParseException {
        // get time
        String date = reader.get(Constants.THEADER_DATE);
        String startT = reader.get(Constants.THEADER_TIME_START);
        String endT = reader.get(Constants.THEADER_TIME_END);
        entry.startTime = Constants.INPUT_DATE_TIME_FORMATTER.parse(date + " " + startT);
        entry.endTime = Constants.INPUT_DATE_TIME_FORMATTER.parse(date + " " + endT);
        if (entry.endTime.before(entry.startTime)) {
            // this drive is over midnight -> add 24 hours
            entry.endTime.setTime(entry.endTime.getTime() + 86400000);
        }

        // get route
        entry = parseRouteEntry(entry, reader);

        // get rest
        entry.reason = reader.get(Constants.THEADER_REASON);
        entry.person = reader.get(Constants.THEADER_PERSON);
        entry.kmEnd = Integer.parseInt(reader.get(Constants.THEADER_KM_COUNTER));
        switch (reader.get(Constants.THEADER_TYPE).toUpperCase()) {
            case Constants.TYPE_PRIVATE_STRING:
                entry.type = Constants.TYPE_PRIVATE;
                break;
            case Constants.TYPE_WORK_STRING:
                entry.type = Constants.TYPE_WORK;
                break;
            case Constants.TYPE_OFFICE_STRING:
                entry.type = Constants.TYPE_OFFICE;
                break;
            default:
                Logger.getLogger(CsvInformationParser.class.getName())
                        .log(Level.WARNING, "Unknwon Type! {0}",
                                reader.get(Constants.THEADER_TYPE));

        }
        String fuelString = reader.get(Constants.THEADER_FUEL);
        if (!fuelString.isEmpty()) {
            entry.fuelAmount = Double.parseDouble(fuelString.replaceAll(",", "."));
        }

        return entry;

    }

    /**
     *
     * @param entry
     * @param reader
     * @return
     * @throws IOException
     */
    private static DataEntry parseExtendedFields(DataEntry entry, CsvReader reader) throws IOException {
        entry.marker = Marker.fromCsvString(reader.get(Constants.THEADER_MARKER));
        entry.fuelConsumption = Double.parseDouble(
                reader.get(Constants.THEADER_FUEL_CONSUMPTION).replaceAll(",", "."));
        return entry;
    }

    private static DataEntry parseRouteEntry(DataEntry entry, CsvReader reader) throws IOException {
        Route route = new Route();
        route.start = new Location(reader.get(Constants.THEADER_LOCATION_START));
        route.end = new Location(reader.get(Constants.THEADER_LOCATION_END));
        route.km = Integer.parseInt(reader.get(Constants.THEADER_KM));

        route.detours = new ArrayList<>();
        String detoursString = reader.get(Constants.THEADER_LOCATION_DETOURS);
        if (!detoursString.isEmpty()) {
            String[] detoursArray = detoursString.split(Constants.DETOURS_DELIMITER);
            for (String item : detoursArray) {
                if (!item.isEmpty()) {
                    route.detours.add(new Location(item));
                }
            }
        }

        entry.route = route;
        // init dummy route table for later
        entry.route.typicalTimes = new RouteTimeTable();

        switch (RouteTimeTable.getZoneOfTime(entry.startTime)) {
            case Constants.TIME_ZONE_1:
                entry.route.typicalTimes.timeZone1
                        = calculateDurationForZone(entry.startTime.getTime(),
                                entry.endTime.getTime());
                break;
            case Constants.TIME_ZONE_2:
                entry.route.typicalTimes.timeZone2
                        = calculateDurationForZone(entry.startTime.getTime(),
                                entry.endTime.getTime());
                break;
            case Constants.TIME_ZONE_3:
                entry.route.typicalTimes.timeZone3
                        = calculateDurationForZone(entry.startTime.getTime(),
                                entry.endTime.getTime());
                break;
            default:
                throw new IllegalStateException("PROGRAMMIERFEHLER");
        }

        return entry;
    }
}
