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
public class RepairShop implements Target {
    private final Date startDate;
    private final Date endDate;
    private final Location location;
    private final int km;
    private final int type;

    
    
    // km are hard limits
    // fahrt vorher und nachher erzeugen
    // bei typ reifenwechsel, nachziehen ohne termin einfügen

    public RepairShop(Date startDate, Date endDate, Location location, int km, int type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.km = km;
        this.type = type;
    }
}
