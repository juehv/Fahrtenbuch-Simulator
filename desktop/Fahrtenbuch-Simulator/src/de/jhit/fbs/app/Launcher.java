/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.app;

import de.jhit.fbs.ztests.Task;

/**
 *
 * @author Jens
 */
public class Launcher {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
//       Task.testVisualization();
       
       Task.testValidation();
       
       Task.testRandomizer();
    }
}
