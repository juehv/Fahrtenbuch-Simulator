/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.csv;

import com.csvreader.CsvWriter;
import de.jhit.fbs.container.Constants;
import de.jhit.fbs.container.DataEntry;
import de.jhit.fbs.container.RawBook;
import de.jhit.fbs.container.Route;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jens
 */
public class CsvSheetWriter {

    private static final String[] QUESTION_SHEET_HEADER = {
        Constants.THEADER_MARKER,
        Constants.THEADER_LOCATION_START,
        Constants.THEADER_LOCATION_END,
        Constants.THEADER_LOCATION_DETOURS,
        Constants.THEADER_KM,
        Constants.THEADER_TIME_Z1,
        Constants.THEADER_TIME_Z2,
        Constants.THEADER_TIME_Z3};

    private static final String[] SUGGESTION_BOOK_HEADER = {
        Constants.THEADER_MARKER,
        Constants.THEADER_DATE,
        Constants.THEADER_TIME_START,
        Constants.THEADER_TIME_END,
        Constants.THEADER_DURATION,
        Constants.THEADER_LOCATION_START,
        Constants.THEADER_LOCATION_DETOURS,
        Constants.THEADER_LOCATION_END,
        Constants.THEADER_REASON,
        Constants.THEADER_PERSON,
        Constants.THEADER_KM,
        Constants.THEADER_KM_COUNTER,
        Constants.THEADER_FUEL,
        Constants.THEADER_FUEL_CONSUMPTION,
        Constants.THEADER_TYPE,
        Constants.THEADER_SYSTEM_COMMENT
    };

    public static boolean writeQuestionSheet(String pathToQuestionCsv, List<Route> questionRoutes) {
        CsvWriter cwriter = new CsvWriter(pathToQuestionCsv, ',', Charset.forName("UTF-8"));
        //Sorting
        Collections.sort(questionRoutes, Route.getComparator());

        try {
            // write header
            // cant insert comments until found out how to skip them
            //TODO write comments for better understanding of marker
//            cwriter.writeComment("Please fill the table.");
//            cwriter.writeComment("Values marked with \"!!-\" are duplicates with different values. Please remove the wrong one.");
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

    public static boolean writeNewStaticFile(String path, List<Route> knownRoutes) {
        // read static file
        // add ne entrys
        // done
        return true;
    }

    public static boolean writeSuggestedBook(String pathToSuggestionCsv, RawBook book) {
        CsvWriter cwriter = new CsvWriter(pathToSuggestionCsv, ',', Charset.forName("UTF-8"));

        try {
            // write header
            // cant insert comments until found out how to skip them
//            cwriter.writeComment("Please fill the table.");
//            cwriter.writeComment("Values marked with \"!!-\" are duplicates with different values. Please remove the wrong one.");
            cwriter.writeRecord(SUGGESTION_BOOK_HEADER);
            // write entries
            for (DataEntry item : book.entrys) {
                item.toCsvSuggestionLine(cwriter);
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
}
