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
public class PartnerVisit implements Target{
    private final Date startDate;
    private final Date endDate;
    private final Location location;

    public PartnerVisit(Date startDate, Date endDate, Location location) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
    
    // in regelmäßigen abständen einstreuen als besprechungen insbesondere vor aufträgen
    
}
