/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.app;

import de.jhit.fbs.container.Entry;
import de.jhit.fbs.view.HtmlPrinter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jens
 */
public class Launcher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        List<Entry> entrys = new ArrayList<>();
        entrys.add(new Entry());
        entrys.add(new Entry());
        entrys.add(new Entry());
        entrys.add(new Entry());
        
       new HtmlPrinter().print(entrys);
    }
}
