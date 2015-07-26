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
public class CustomerVisit implements Target{
    private final Date startDate;
    private final Date endDate;
    private final Location location;

    public CustomerVisit(Date startDate, Date endDate, Location location) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
    
    // prinzipiell von zuhause starten und zum büro werkzeug holen
    // fahrt vorher und nachher erzeugen
    // ghost fahrten zwischen den terminen (für service, vorbesprechung)
}
