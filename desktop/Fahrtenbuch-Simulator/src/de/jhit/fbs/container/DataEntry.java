/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import com.csvreader.CsvWriter;
import de.jhit.fbs.analyser.RouteAnalyzer;
import de.jhit.fbs.csv.CsvInformationParser;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Jens
 */
public class DataEntry {

    // TODO change marker to a list
    public Marker marker = new Marker();
    public Date startTime;
    public Date endTime;
    public Route route;
    public String reason;
    public String person;
    public int kmEnd;
    public int type;

    public int fuelKmAmount;
    public double fuelAmount;
    public double fuelConsumption;

    @Override
    public String toString() {
        return "DataEntry{" + "startTime=" + startTime + ", endTime=" + endTime + ", route=" + route + ", reason=" + reason + ", person=" + person + ", kmEnd=" + kmEnd + ", type=" + type + ", fuelAmount=" + fuelAmount + '}';
    }

    public CsvWriter toCsvSuggestionLine(CsvWriter writer) throws IOException {
        // Constants.THEADER_MARKER,
//        Constants.THEADER_TIME_START,
//        Constants.THEADER_TIME_END,
//        Constants.THEADER_DURATION,
//        Constants.THEADER_LOCATION_START,
//        Constants.THEADER_LOCATION_DETOURS,
//        Constants.THEADER_LOCATION_END,
//        Constants.THEADER_REASON,
//        Constants.THEADER_PERSON,
//        Constants.THEADER_KM,
//        Constants.THEADER_KM_COUNTER,
//        Constants.THEADER_FUEL,
//        Constants.THEADER_FUEL_CONSUMPTION,
//        Constants.THEADER_TYPE};
        writer.write(marker.toCsvString());
        writer.write(Constants.OUTPUT_DATE_FORMATTER.format(startTime));
        writer.write(Constants.OUTPUT_TIME_FORMATTER.format(startTime));
        writer.write(Constants.OUTPUT_TIME_FORMATTER.format(endTime));
        writer.write(String.valueOf(CsvInformationParser.calculateDurationForZone(
                startTime.getTime(), endTime.getTime())));
        writer.write(route.start.toString());
        if (route.detours != null && !route.detours.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Location item : route.detours) {
                sb.append(item.toString()).append(Constants.DETOURS_DELIMITER);
            }
            writer.write(sb.toString());
        } else {
            writer.write("");
        }
        writer.write(route.end.toString());
        writer.write(reason);

        writer.write(person);
        writer.write(String.valueOf(route.km));
        writer.write(String.valueOf(kmEnd));
        writer.write(String.valueOf(fuelAmount));
        writer.write(String.valueOf(fuelConsumption));

        switch (type) {
            case Constants.TYPE_OFFICE:
                writer.write(Constants.TYPE_OFFICE_STRING);
                break;
            case Constants.TYPE_PRIVATE:
                writer.write(Constants.TYPE_PRIVATE_STRING);
                break;
            case Constants.TYPE_WORK:
                writer.write(Constants.TYPE_WORK_STRING);
                break;
        }

        return writer;
    }

    public static Comparator<DataEntry> getComparator() {
        return new Comparator<DataEntry>() {
            @Override
            public int compare(DataEntry a, DataEntry b) {
                return a.startTime.compareTo(b.startTime);
            }
        };
    }
}
