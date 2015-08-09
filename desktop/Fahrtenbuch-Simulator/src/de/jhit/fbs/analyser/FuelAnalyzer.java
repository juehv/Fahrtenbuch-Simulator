/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.analyser;

import de.jhit.fbs.container.RawBook;

/**
 *
 * @author Jens
 */
public class FuelAnalyzer {

    public static RawBook calculateFuelConsumption(RawBook book) {
        // find blocks

        // calculate kilometer
        // calculate consumption
        return book;
    }

    public static void calculateMissingInformation(RawBook book, int usualConsumtionRangeStart, int usualConsumtionRangeEnd) {
        // finds unusual consumtions
        // suggest number of mussing fuel
        // suggest number of missing kilometer
        // add sugestions to book and wirte question (which contails calculations as temp )csv to review the book
    }
}
