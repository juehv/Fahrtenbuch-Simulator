/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.csv;

import com.csvreader.CsvWriter;
import de.jhit.fbs.container.Route;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class CsvSheetWriter {

    private static final String[] QUESTION_SHEET_HEADER = {"Marker", "Start L", "End L", "Detours", "km", "Time 6-9", "Time 16-19", "Time else"};

    public static boolean writeQuestionSheet(String pathToQuestionCsv, List<Route> questionRoutes) {
        CsvWriter cwriter = new CsvWriter(pathToQuestionCsv, ',', Charset.forName("UTF-8"));
        //Sorting
        Collections.sort(questionRoutes, new Comparator<Route>() {
            @Override
            public int compare(Route a, Route b) {
                int result = a.start.point.compareTo(b.start.point);
                if (result == 0) {
                    result = a.end.point.compareTo(b.end.point);
                    if (result == 0
                            && (a.detours != null && b.detours != null)
                            && a.detours.size() != b.detours.size()) {
                        if (a.detours.size() > b.detours.size()) {
                            result = -1;
                        } else {
                            result = 1;
                        }
                    }
                }
                return result;
            }
        });

        try {
            // write header
            cwriter.writeComment("Please fill the table.");
            cwriter.writeComment("Values marked with \"!!-\" are duplicates with different values. Please remove the wrong one.");
            cwriter.writeRecord(QUESTION_SHEET_HEADER);
            // write questions
            for (Route item : questionRoutes) {
                item.toCsvQuestionLine(cwriter);
                cwriter.endRecord();
            }


            return true;
        } catch (IOException ex) {
            Logger.getLogger(CsvSheetWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            cwriter.close();
        }
        return false;
    }

    public static boolean writeAnswersToStaticFile(String path) {
        // read static file
        // add ne entrys
        // done
        return true;
    }
}