/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.generators;

import java.util.Random;

/**
 *
 * @author Jens
 */
public class WayLengthRandomizer {

    private static final int[] deviation = {-1, 0, 0, 0, 1, 1, 1};

    public static int getRandomDeviation() {
        return deviation[new Random().nextInt(deviation.length)];
    }
}
