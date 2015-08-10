/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.analyser;

import de.jhit.fbs.container.DataEntry;
import de.jhit.fbs.container.Location;
import de.jhit.fbs.container.Constants;
import de.jhit.fbs.container.RawBook;
import de.jhit.fbs.container.Route;
import de.jhit.fbs.smartcontainer.RouteTimeTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jens
 */
public class FuelAnalyzer {

    public static RawBook calculateFuelConsumption(RawBook book) {
        // find blocks
        List<List<DataEntry>> blocks = new ArrayList<>();
        List<DataEntry> currentBlock = new ArrayList<>();

        for (DataEntry item : book.entrys) {
            currentBlock.add(item);
            if (item.fuelAmount != 0.0) {
                blocks.add(currentBlock);
                currentBlock = new ArrayList<>();
            }
        }

        // block processing
        for (List<DataEntry> block : blocks) {
            int kmAmount = 0;
            for (DataEntry entry : block) {
                kmAmount += entry.route.km;
            }
            block.get(block.size() - 1).fuelKmAmount = kmAmount;
            block.get(block.size() - 1).fuelConsumption = (double) (Math.round(((block.get(block.size() - 1).fuelAmount * 100)
                    / kmAmount) * 100)) / 100;
        }

        return book;
    }

    public static RawBook calculateMissingInformation(RawBook book, int usualConsumtionRangeMin, int usualConsumtionRangeMax) {
        // finds unusual consumtions and create suggestion entrys
        double avgConsumtion = (double) (usualConsumtionRangeMin + usualConsumtionRangeMax) / 2;
        ArrayList<DataEntry> newEntrys = new ArrayList<>();
        for (DataEntry item : book.entrys) {
            if (item.fuelAmount != 0) {
                if (usualConsumtionRangeMin > item.fuelConsumption) {
                    // unusual less consumtion -> suggest missing fuel
                    double missingFuel = (double) (Math.round(
                            avgConsumtion * item.fuelKmAmount)
                            - (item.fuelAmount * 100)) / 100;
                    newEntrys.add(createSuggestionBlockBeforeDate(item.startTime,
                            "MISSING FUEL", 0, missingFuel));
                } else if (item.fuelConsumption > usualConsumtionRangeMax) {
                    // unsusual much consumtion -> suggest missing km
                    int missingKm = (int) Math.round(((item.fuelAmount
                            / avgConsumtion) * 100) - item.fuelKmAmount);
                    newEntrys.add(createSuggestionBlockBeforeDate(item.startTime,
                            "MISSING KM", missingKm, 0));
                }
            }
        }

        // insert new entrys
        book.entrys.addAll(newEntrys);
        Collections.sort(book.entrys, DataEntry.getComparator());

        return book;
    }

    private static DataEntry createSuggestionBlockBeforeDate(Date entryDate, String reasonText, int km, double fuel) {
        DataEntry newEntry = new DataEntry();
        newEntry.marker.add(Constants.SUGGESTION_MARKER);
        newEntry.startTime = new Date(entryDate.getTime() - 600000); // 10 min before
        newEntry.endTime = newEntry.startTime;
        newEntry.fuelAmount = fuel;
        newEntry.reason = "SUGGESTION";
        newEntry.person = reasonText;

        newEntry.route = new Route();
        newEntry.route.start = new Location("");
        newEntry.route.end = new Location("");
        newEntry.route.detours = new ArrayList<>();
        newEntry.route.typicalTimes = new RouteTimeTable();
        newEntry.route.km = km;

        return newEntry;
    }
}
