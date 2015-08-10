/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import com.csvreader.CsvWriter;
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
        // "Constants", "Start T", "End T", "Start L", "Detours","End L","Reason","Person",  "km", "km Counter (end)", "Fuel", "Type"
        writer.write(marker.toCsvString());
        DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
        writer.write(dateFormatter.format(startTime));
        writer.write(dateFormatter.format(endTime));
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
                writer.write("office");
                break;
            case Constants.TYPE_PRIVATE:
                writer.write("private");
                break;
            case Constants.TYPE_WORK:
                writer.write("work");
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
