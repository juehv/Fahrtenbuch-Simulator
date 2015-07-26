/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.target;

import de.jhit.fbs.container.Location;
import java.util.Date;

/**
 *
 * @author Jens
 */
public class Fuel implements Target{
    private final Date date;
    private final Location location;

    public Fuel(Date date, Location location) {
        this.date = date;
        this.location = location;
    }
    
    // wege von und zur tankstelle sind betriebswege
    // wege möglichs realistisch auf fahrten platzieren
    // verbrauch analysieren
    // 
    
}
