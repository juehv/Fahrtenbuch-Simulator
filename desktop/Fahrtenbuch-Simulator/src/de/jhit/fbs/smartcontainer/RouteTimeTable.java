/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.smartcontainer;

import com.csvreader.CsvWriter;
import de.jhit.fbs.container.Constants;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class RouteTimeTable {

    public int timeZone1; //6-9
    public int timeZone2; //16-19
    public int timeZone3; //else

    public int getTypicalTimeForTimepoint(Date timepoint) {
        return 0;
    }

    public boolean isTimeFotTimepointCredible(Date timepoint, int time) {
        return true;
    }

    public int getRandomTypicalTimefoTimepoint(Date timepoint) {
        throw new IllegalAccessError();
    }

    public CsvWriter toCsvQuestionLine(CsvWriter writer) throws IOException {
        writer.write(String.valueOf(timeZone1));
        writer.write(String.valueOf(timeZone2));
        writer.write(String.valueOf(timeZone3));

        return writer;
    }

    public static int getZoneOfTime(Date timestamp) {
        Date start;
        Date end;
        try {
            // Get date of timestamp
            Date day = Constants.OUTPUT_TIME_FORMATTER.parse(
                    Constants.OUTPUT_TIME_FORMATTER.format(timestamp));
            // Zone 1
            start = new Date(day.getTime() + 21600000); // 6
            end = new Date(day.getTime() + 32400000); //9
            if (timestamp.after(start) && timestamp.before(end)) {
                return Constants.TIME_ZONE_1;
            }
            // Zone 2
            start = new Date(day.getTime() + 57600000); // 16
            end = new Date(day.getTime() + 68400000); //19
            if (timestamp.after(start) && timestamp.before(end)) {
                return Constants.TIME_ZONE_2;
            }
            // Zone 3
            // if other doesnt match use zone 3
            return Constants.TIME_ZONE_3;
        } catch (ParseException ex) {
            Logger.getLogger(RouteTimeTable.class.getName()).log(Level.SEVERE,
                    "very unexpected!", ex);
        }
        return Constants.TIME_ZONE_3; // return 3 if unknown
    }
}
