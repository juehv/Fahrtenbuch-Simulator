/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.analyser;

import de.jhit.fbs.container.DataEntry;
import de.jhit.fbs.container.RawBook;
import de.jhit.fbs.container.Route;
import de.jhit.fbs.smartcontainer.RouteTimeTable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jens
 */
public class RouteAnalyzer {

    public static final String DUPLICATE_MARKER = "!!";

    public static List<Route> returnSingleRoutesForQuestions(RawBook book,
            List<Route> knownRoutes) {
        ArrayList<Route> singleRoutes = new ArrayList<>();
        for (DataEntry item : book.entrys) {
            if (knownRoutes.contains(item.route)) {
                // known route -> check if values are different
                if (routesAreDifferentAndUpdate(item.route, knownRoutes.get(knownRoutes.indexOf(item.route)))) {
                    // add it as marked duplicate
                    item.route.marker += DUPLICATE_MARKER;
                    singleRoutes.add(item.route);
                }
            } else if (singleRoutes.contains(item.route)) {
                // already seen -> check if values are different
                if (routesAreDifferentAndUpdate(item.route, singleRoutes.get(singleRoutes.indexOf(item.route)))) {
                    // add it as marked duplicate
                    item.route.marker += DUPLICATE_MARKER;
                    singleRoutes.add(item.route);
                }
            } else {
                // unknown route
                singleRoutes.add(item.route);
            }
        }

        // TODO if contains
        // check if km diverts more than +/-1 km
        // check if time is in a unknown zone and if it is
        // check if time diverts more thant +/- 10 minutes form knwon time

        return singleRoutes;
    }

    private static boolean routesAreDifferentAndUpdate(Route newRoute, Route knownRoute) {
        boolean retval = false;
        // check time zones and update if possible
        knownRoute.typicalTimes.timeZone1 = updateZones(
                newRoute.typicalTimes.timeZone1,
                knownRoute.typicalTimes.timeZone1);
        retval |= isDivertionOutOfRange(newRoute.typicalTimes.timeZone1,
                knownRoute.typicalTimes.timeZone1, 10);

        knownRoute.typicalTimes.timeZone2 = updateZones(
                newRoute.typicalTimes.timeZone2,
                knownRoute.typicalTimes.timeZone2);
        retval |= isDivertionOutOfRange(newRoute.typicalTimes.timeZone2,
                knownRoute.typicalTimes.timeZone2, 10);

        knownRoute.typicalTimes.timeZone3 = updateZones(
                newRoute.typicalTimes.timeZone3,
                knownRoute.typicalTimes.timeZone3);
        retval |= isDivertionOutOfRange(newRoute.typicalTimes.timeZone3,
                knownRoute.typicalTimes.timeZone3, 10);

        // check km
        if (knownRoute.km + 1 < newRoute.km
                && newRoute.km < knownRoute.km - 1) {
            // km diverts more than +/- 1 km -> needs review
            retval = true;
        }
        return retval;
    }

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

    private static boolean isDivertionOutOfRange(int newZone, int knownZone, int range) {
        if (knownZone != 0 && newZone != 0) {
            // knwon zone
            if (knownZone + range < newZone
                    && newZone < knownZone - range) {
                // other value? --> needs review
                return true;
            }
        }
        return false;
    }

    public static RawBook updateRoutes(RawBook book, List<Route> knownRoutes, List<Route> questionRoutes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
