/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import com.csvreader.CsvWriter;
import static de.jhit.fbs.container.Route.DETOURS_DELIMITER;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Jens
 */
public class DataEntry {

    public static final int TYPE_PRIVATE = 0;
    public static final int TYPE_OFFICE = 1;
    public static final int TYPE_WORK = 2;
    public String marker = "";
    public Date startTime;
    public Date endTime;
    public Route route;
    public String reason;
    public String person;
    public int kmStart;
    public int kmEnd;
    public int type;
    public double fuelAmount;
    public double fuelMoney;

    @Override
    public String toString() {
        return "DataEntry{" + "startTime=" + startTime + ", endTime=" + endTime + ", route=" + route + ", reason=" + reason + ", person=" + person + ", kmStart=" + kmStart + ", kmEnd=" + kmEnd + ", type=" + type + ", fuelAmount=" + fuelAmount + ", fuelMoney=" + fuelMoney + '}';
    }

    public CsvWriter toCsvSuggestionLine(CsvWriter writer) throws IOException {
        // "Marker", "Start T", "End T", "Start L", "Detours","End L","Reason","Person",  "km", "km Counter (end)", "Fuel", "Type"
        writer.write(marker);
        DateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
        writer.write(dateFormatter.format(startTime));
        writer.write(dateFormatter.format(endTime));
        writer.write(route.start.toString());
        if (route.detours != null && !route.detours.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Location item : route.detours) {
                sb.append(item.toString()).append(DETOURS_DELIMITER);
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

        switch (type) {
            case TYPE_OFFICE:
                writer.write("office");
                break;
            case TYPE_PRIVATE:
                writer.write("private");
                break;
            case TYPE_WORK:
                writer.write("work");
                break;
        }

        return writer;
    }
}
