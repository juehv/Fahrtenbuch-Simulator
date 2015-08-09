/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import java.util.Date;

/**
 *
 * @author Jens
 */
public class DataEntry {

    public static final int TYPE_PRIVATE = 0;
    public static final int TYPE_OFFICE = 1;
    public static final int TYPE_WORK = 1;
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
}
