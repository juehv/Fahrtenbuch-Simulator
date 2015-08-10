/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.csv;

import com.csvreader.CsvReader;
import de.jhit.fbs.container.Constants;
import de.jhit.fbs.container.Marker;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jens
 */
public class CsvValidator {

    /**
     * Checks if at least enough lines for simple header are there and a date
     * ist present (and it is no comment because the silly lib cant do that for
     * me -.-)
     */
    private static final Pattern validEntry = Pattern.compile("^[^#]*\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d,.*,.*,.*,.*,.*,.*,.*,.*,.*,.*,.*,.*");

    public static boolean validateSimpleBookHeader(CsvReader reader) throws IOException {
        boolean result = true;
        Set<String> header = new TreeSet<>(Arrays.asList(reader.getHeaders()));
        result &= header.contains(Constants.THEADER_DATE);
        result &= header.contains(Constants.THEADER_TIME_START);
        result &= header.contains(Constants.THEADER_TIME_END);
        result &= header.contains(Constants.THEADER_LOCATION_START);
        result &= header.contains(Constants.THEADER_LOCATION_END);
        result &= header.contains(Constants.THEADER_LOCATION_DETOURS);
        result &= header.contains(Constants.THEADER_REASON);
        result &= header.contains(Constants.THEADER_PERSON);
        result &= header.contains(Constants.THEADER_KM);
        result &= header.contains(Constants.THEADER_KM_COUNTER);
        result &= header.contains(Constants.THEADER_FUEL);
        result &= header.contains(Constants.THEADER_TYPE);
        return result;
    }

    public static boolean validateBookHeaderExtentions(CsvReader reader) throws IOException {
        boolean result = true;
        Set<String> header = new TreeSet<>(Arrays.asList(reader.getHeaders()));
        result &= header.contains(Constants.THEADER_MARKER);
        result &= header.contains(Constants.THEADER_FUEL_CONSUMPTION);
        return result;
    }

    public static boolean validateCsvEntry(String entryLine) {
        return validEntry.matcher(entryLine).matches();
    }

    public static boolean isMarkerValid(Marker marker) {
        if (marker.contains(Constants.DUPLICATE_MARKER)
                || marker.contains(Constants.SUGGESTION_MARKER)) {
            return false;
        } else {
            return true;
        }
    }

}
