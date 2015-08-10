/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.analyser;

import de.jhit.fbs.container.DataEntry;
import de.jhit.fbs.container.Constants;
import de.jhit.fbs.container.Location;
import de.jhit.fbs.container.RawBook;
import de.jhit.fbs.container.Route;
import de.jhit.fbs.csv.CsvInformationParser;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class RouteAnalyzer {

    public static final int TIME_DIVERSION_RANGE = 10;

    public static List<Route> returnSingleRoutesForQuestions(List<Route> newRoutes, List<Route> knownRoutes) {
        return markIfRouteIsInDbAndUpdate(returnSingleRoutes(newRoutes), knownRoutes);
    }

    public static List<Route> returnSingleRoutes(List<Route> newRoutes) {
        ArrayList<Route> singleRoutes = new ArrayList<>();
        // first use just the given data and count the routes
        for (Route item : newRoutes) {
            if (singleRoutes.contains(item)) {
                // already seen -> check if values are different and count the route
                singleRoutes.get(singleRoutes.indexOf(item)).appearanceCount++;
                if (routesAreDifferentAndUpdate(item, singleRoutes.get(singleRoutes.indexOf(item)))) {
                    // add it as marked duplicate
                    item.marker.add(Constants.DUPLICATE_MARKER);
                    singleRoutes.add(item);
                }
            } else {
                // unknown route
                singleRoutes.add(item);
            }
        }

        return singleRoutes;
    }

    public static List<Route> markIfRouteIsInDbAndUpdate(List<Route> newRoutes,
            List<Route> knownRoutes) {
        // second check if we have knwledge in the "db"
        ArrayList<Route> tmpRoutes = new ArrayList<>();
        for (Route item : newRoutes) {
            if (knownRoutes.contains(item)) {

                // known route -> check if values are different
                if (routesAreDifferentAndUpdate(item, knownRoutes.get(knownRoutes.indexOf(item)))) {
                    // mark it as devil
                    item.marker.add(Constants.OTHER_VALUES_MARKER);
                    // add db entry
                    Route dbDuplicate = knownRoutes.get(knownRoutes.indexOf(item));
                    dbDuplicate.marker.add(Constants.DB_VALUES_MARKER);
                    tmpRoutes.add(dbDuplicate);
                } else {
                    // mark as good values
                    item.marker.add(Constants.DB_VALUES_MARKER);
                }
            }
        }
        tmpRoutes.addAll(newRoutes);
        // sort
        Collections.sort(tmpRoutes, Route.getComparator());
        return tmpRoutes;
    }

    private static boolean routesAreDifferentAndUpdate(Route newRoute, Route knownRoute) {
        boolean retval = false;
        // check time zones and update if possible
        knownRoute.typicalTimes.timeZone1 = updateZones(
                newRoute.typicalTimes.timeZone1,
                knownRoute.typicalTimes.timeZone1);
        retval |= isTimeDivertionOutOfRange(newRoute.typicalTimes.timeZone1,
                knownRoute.typicalTimes.timeZone1, TIME_DIVERSION_RANGE);

        knownRoute.typicalTimes.timeZone2 = updateZones(
                newRoute.typicalTimes.timeZone2,
                knownRoute.typicalTimes.timeZone2);
        retval |= isTimeDivertionOutOfRange(newRoute.typicalTimes.timeZone2,
                knownRoute.typicalTimes.timeZone2, TIME_DIVERSION_RANGE);

        knownRoute.typicalTimes.timeZone3 = updateZones(
                newRoute.typicalTimes.timeZone3,
                knownRoute.typicalTimes.timeZone3);
        retval |= isTimeDivertionOutOfRange(newRoute.typicalTimes.timeZone3,
                knownRoute.typicalTimes.timeZone3, TIME_DIVERSION_RANGE);

        // check km
        if (knownRoute.km + 1 < newRoute.km
                && newRoute.km < knownRoute.km - 1) {
            // km diverts more than +/- 1 km -> needs review
            retval = true;
        }
        return retval;
    }

    // TODO mv to util class
    private static int updateZones(int newZone, int knownZone) {
        if (knownZone == 0) {
            // unknown zone -> update?
            if (newZone != 0) {
                // new value -> update
                return newZone;
            } else {
                return 0;
            }
        } else {
            return knownZone;
        }
    }

    // TODO mv to util class
    private static boolean isTimeDivertionOutOfRange(int newTime, int knownTime, int range) {
        if (knownTime != 0 && newTime != 0) {
            // knwon zone
            if (knownTime + range < newTime
                    || newTime < knownTime - range) {
                // other value? --> needs review
                return true;
            }
        }
        return false;
    }

    public static RawBook updateRoutes(RawBook book, List<Route> knownRoutes) {

        for (DataEntry item : book.entrys) {
            if (knownRoutes.contains(item.route)) {
                // known route -> check if values are different
                Route tmpRoute = knownRoutes.get(knownRoutes.indexOf(item.route));
                item.route.km = tmpRoute.km;
                item.route.typicalTimes = tmpRoute.typicalTimes;
            } else {
                // unknown route -> should not happen
                Logger.getLogger(RouteAnalyzer.class.getName())
                        .log(Level.SEVERE,
                                "Can''t find the route in knowledge base! Exit.\n{0}",
                                item.route.toString());
                System.exit(-1);
            }
        }

        return book;
    }

    public static RawBook checkRouteTimes(RawBook book) {

        boolean isUnusualTime;
        for (DataEntry entry : book.entrys) {

            if (entry.startTime.getHours() >= 6
                    && entry.startTime.getHours() <= 9) {
                // Zone 1
                isUnusualTime = isTimeDivertionOutOfRange(
                        CsvInformationParser.calculateDurationForZone(
                                entry.startTime.getTime(),
                                entry.endTime.getTime()),
                        entry.route.typicalTimes.timeZone1, TIME_DIVERSION_RANGE);
            } else if (entry.startTime.getHours() >= 16
                    && entry.startTime.getHours() <= 18) {
                // Zone 2
                isUnusualTime = isTimeDivertionOutOfRange(
                        CsvInformationParser.calculateDurationForZone(
                                entry.startTime.getTime(),
                                entry.endTime.getTime()),
                        entry.route.typicalTimes.timeZone2, TIME_DIVERSION_RANGE);
            } else {
                // Zone 3
                isUnusualTime = isTimeDivertionOutOfRange(
                        CsvInformationParser.calculateDurationForZone(
                                entry.startTime.getTime(),
                                entry.endTime.getTime()),
                        entry.route.typicalTimes.timeZone3, TIME_DIVERSION_RANGE);
            }

            if (isUnusualTime) {
                entry.marker.add(Constants.TIME_WARNING_MARKER);
            }
        }

        return book;
    }

    public static RawBook checkRouteWaypoints(RawBook book) {
        for (int i = 0; i < book.entrys.size() - 1; i++) {
            if (!book.entrys.get(i).route.end.equals(
                    book.entrys.get(i + 1).route.start)) {
                book.entrys.get(i).marker.add(Constants.WAYPOINT_WARNING_MARKER);
                book.entrys.get(i + 1).marker.add(Constants.WAYPOINT_WARNING_MARKER);
            }
        }

        return book;
    }

    public static RawBook checkKilometerCounter(RawBook book) {

        int lastCounter = -1;
        for (DataEntry item : book.entrys) {
            if (lastCounter == -1) {
                // first entry. can't be checked
                lastCounter = item.kmEnd;
            }

        }

        return book;
    }

    
    public static Map<String, String> generateRouteShortcuts(List<Route> routes) {
        Map<String, String> shortcuts = new HashMap<>();
        Map<String, Integer> directory = new HashMap<>();
        routes = returnSingleRoutes(routes);
        // find all waypoints
        for (Route item : routes) {
            updateDirectory(directory, item.start.toString(), item.appearanceCount);
            updateDirectory(directory, item.end.toString(), item.appearanceCount);
            for (Location subItem : item.detours) {
                updateDirectory(directory, subItem.toString(), item.appearanceCount);
            }
        }
        // generate shortcuts
        for (String item: directory.keySet()){
            if (directory.get(item) > Constants.APPEARANCE_COUNT_FOR_SHORTCUT){
                // generate shortcut name
                // remove vocals from city name
                // add an integer --> need directory for that
                String shortcutKey = "";
                // create shortcut
                shortcuts.put(shortcutKey, item);
                //TODO finish implementation
            }
        }
        return shortcuts;
    }

    private static void updateDirectory(Map<String, Integer> directory,
            String key, int value) {
        if (directory.containsKey(key)) {
            // update count
            directory.put(key, directory.get(key) + value);
        } else {
            // new entry
            directory.put(key, value);
        }
    }
}
