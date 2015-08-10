/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.view;

import de.jhit.fbs.container.Constants;
import de.jhit.fbs.container.DataEntry;
import de.jhit.fbs.container.Location;
import de.jhit.fbs.container.VisualizationEntry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jens
 */
public class PrintableEntryFactory {

    public static List<VisualizationEntry> convertEntrys(List<DataEntry> entrys) {
        List<VisualizationEntry> newEntrys = new ArrayList<>();

        for (DataEntry item : entrys) {
            VisualizationEntry newVEntry = new VisualizationEntry();
            newVEntry.date = Constants.OUTPUT_DATE_FORMATTER.format(item.startTime);
            newVEntry.time
                    = Constants.OUTPUT_TIME_FORMATTER.format(item.startTime)
                    + " - "
                    + Constants.OUTPUT_TIME_FORMATTER.format(item.endTime);
            String detoursString = "";
            for (Location locItem : item.route.detours) {
                detoursString += "</li><li>" + locItem.toString() + "</li><li>";
            }
            newVEntry.target = "<ul style=\"list-style-type:none;\"><li>"
                    + item.route.start.toString() + "</li><li>"
                    + detoursString
                    + item.route.end.toString() + "</li></ul>";
            newVEntry.reason = item.reason;
            newVEntry.person = item.person;
            newVEntry.kmStart = String.valueOf(item.kmEnd - item.route.km);
            newVEntry.kmEnd = String.valueOf(item.kmEnd);
            if (item.fuelAmount > 0) {
                newVEntry.gasAmount = String.valueOf(item.fuelAmount);
            }

            switch (item.type) {
                case Constants.TYPE_PRIVATE:
                    newVEntry.kmPrivate = String.valueOf(item.route.km);
                    break;
                case Constants.TYPE_WORK:
                    newVEntry.kmFirm = String.valueOf(item.route.km);
                    break;
                case Constants.TYPE_OFFICE:
                    newVEntry.kmOffice = String.valueOf(item.route.km);
                    break;
            }
            newEntrys.add(newVEntry);

        }
        return newEntrys;
    }
}
