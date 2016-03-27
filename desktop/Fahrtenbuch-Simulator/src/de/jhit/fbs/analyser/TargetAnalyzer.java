/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.analyser;

import de.jhit.fbs.container.Constants;
import de.jhit.fbs.container.DataEntry;
import de.jhit.fbs.container.Location;
import de.jhit.fbs.container.RawBook;
import de.jhit.fbs.container.Route;
import de.jhit.fbs.target.BasicTarget;
import de.jhit.fbs.target.FuelTarget;
import de.jhit.fbs.target.KmCounterTarget;
import de.jhit.fbs.target.ReasonedTarget;
import java.util.ArrayList;

/**
 *
 * @author mswin
 */
public class TargetAnalyzer {

    public static RawBook matchTargetsToBook(RawBook book) {
        for (BasicTarget target : book.targets) {
            boolean contains = false;
            for (DataEntry bookEntry : book.entrys) {
                if (Constants.OUTPUT_DATE_FORMATTER.format(target.startTime)
                        .equals(Constants.OUTPUT_DATE_FORMATTER.format(bookEntry.startTime))) {

                    if ((bookEntry.startTime.before(target.startTime)
                            && bookEntry.endTime.after(target.startTime))
                            || (bookEntry.startTime.before(target.endTime)
                            && bookEntry.endTime.after(target.endTime))) {
                        // book is containing an matching entry, check facts :D
                        contains = true;
                        // TODO automatic tour check (for now add warning)
                        bookEntry.marker.add(Constants.TARGET_WARNING_MARKER);
                        bookEntry.systemComment += "Target-Loc: " + target.location + " ";

                        switch (target.type) {
                            case Constants.TARGET_TYPE_FUEL:
                                if (bookEntry.fuelAmount != 0
                                        && bookEntry.fuelAmount
                                        != ((FuelTarget) target).fuelAmount) {
                                    bookEntry.marker
                                            .add(Constants.TARGET_FUEL_WARNING_MARKER);
                                } else {
                                    bookEntry.fuelAmount
                                            = ((FuelTarget) target).fuelAmount;
                                }
                                break;
                            case Constants.TARGET_TYPE_KM_COUNTER:
                                if (bookEntry.kmEnd != ((KmCounterTarget) target).kmEnd) {
                                    bookEntry.marker.add(Constants.TARGET_ERROR_MARKER);
                                    bookEntry.systemComment += "KM: "
                                            + ((KmCounterTarget) target).kmEnd;

                                }
                            //no break here!
                            case Constants.TARGET_TYPE_REASONED:
                                if (!bookEntry.person.equals(((ReasonedTarget) target).person)
                                        || bookEntry.reason.equals(((ReasonedTarget) target).reason)) {
                                    bookEntry.marker.add(Constants.TARGET_WARNING_MARKER);
                                }
                                bookEntry.systemComment += "Person: "
                                        + ((ReasonedTarget) target).person
                                        + "Reason: "
                                        + ((ReasonedTarget) target).reason
                                        + " ";
                                break;
                        }
                    }

                }
            }

            if (!contains) {
                // no matching entry found, insert one
                DataEntry entry = new DataEntry();

                entry.marker.add(Constants.TARGET_MISSING_MARKER);
                entry.startTime = target.startTime;
                entry.endTime = target.endTime;
                entry.route = new Route();
                entry.route.start = target.location;
                entry.route.end = new Location("");
                entry.route.detours = new ArrayList<>();

                switch (target.type) {
                    case Constants.TARGET_TYPE_FUEL:
                        entry.fuelAmount = ((FuelTarget) target).fuelAmount;
                        break;
                    case Constants.TARGET_TYPE_REASONED:
                        entry.person = ((ReasonedTarget) target).person;
                        entry.reason = ((ReasonedTarget) target).reason;
                        break;
                    case Constants.TARGET_TYPE_KM_COUNTER:
                        entry.kmEnd = ((KmCounterTarget) target).kmEnd;
                        entry.person = ((KmCounterTarget) target).person;
                        entry.reason = ((KmCounterTarget) target).reason;
                        break;
                }
                book.entrys.add(entry);
            }
        }

        //TODO sort after date
        return book;
    }
}
