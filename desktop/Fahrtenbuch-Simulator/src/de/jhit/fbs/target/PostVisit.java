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
public class PostVisit implements Target {

    private final Date startDate;
    private final Date endDate;
    private final Location location;

    public PostVisit(Date startDate, Date endDate, Location location) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
    // fahrt vorher und nachher einfügen
    // vorher oft von zuhause aus
    // nachher innerhal weniger tage zum büro
    // vor kundenbesuchen einstreuen
}
