/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.smartcontainer;

import com.csvreader.CsvWriter;
import java.io.IOException;
import java.util.Date;

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
}
