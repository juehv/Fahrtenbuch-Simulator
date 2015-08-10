/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Jens
 */
public class Marker {

    private final HashMap<String, String> marker = new HashMap<>();

    public boolean contains(String markerString) {
        return marker.containsKey(markerString);
    }

    public Marker add(String markerString, String value) {
        marker.put(markerString, value);
        return this;
    }

    public Marker add(String markerString) {
        marker.put(markerString, "");
        return this;
    }

    public String toCsvString() {
        StringBuilder sb = new StringBuilder();
        for (String item : marker.keySet()) {
            sb.append(item);
            if (!marker.get(item).isEmpty()) {
                sb.append(Constants.MARKER_VALUE_DELIMITER)
                        .append(marker.get(item));
            }
            sb.append(Constants.MARKER_DELIMITER);
        }
        return sb.toString();
    }

    public static Marker fromCsvString(String markerString) {
        Marker retval = new Marker();
        String[] markers = markerString.split(Constants.MARKER_DELIMITER);
        for (String item : markers) {
            if (!item.isEmpty()) {
                String[] pieces = item.split(Constants.MARKER_VALUE_DELIMITER);
                if (pieces.length > 1) {
                    retval.marker.put(pieces[0], pieces[1]);
                } else {
                    retval.marker.put(pieces[0], "");
                }
            }
        }
        return retval;
    }
}
