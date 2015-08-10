/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import java.util.ArrayList;

/**
 *
 * @author Jens
 */
public class Marker {

    // TODO string to enum
    private ArrayList<String> marker = new ArrayList<>();

    public boolean contains(String marker) {
        return marker.contains(marker);
    }

    public Marker add(String markerString) {
        marker.add(markerString);
        return this;
    }

    public String toCsvString() {
        StringBuilder sb = new StringBuilder();
        for (String item : marker) {
            sb.append(item).append(Constants.MARKER_DELIMITER);
        }
        return sb.toString();
    }

    public static Marker fromCsvString(String markerString) {
        Marker retval = new Marker();
        String[] markers = markerString.split(Constants.MARKER_DELIMITER);
        for (String item : markers) {
            if (!item.isEmpty()) {
                retval.marker.add(item);
            }
        }
        return retval;
    }
}
