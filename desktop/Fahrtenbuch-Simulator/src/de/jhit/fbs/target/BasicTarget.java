/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.target;

import de.jhit.fbs.container.Location;
import java.util.Date;

/**
 *
 * @author mswin
 */
public class BasicTarget {
    
    public int type;
    public Date startTime;
    public Date endTime;
    public Location location;

    @Override
    public String toString() {
        return "BasicTarget{" + "type=" + type + ", startTime=" + startTime + ", endTime=" + endTime + ", location=" + location + '}';
    }
    
    
}
