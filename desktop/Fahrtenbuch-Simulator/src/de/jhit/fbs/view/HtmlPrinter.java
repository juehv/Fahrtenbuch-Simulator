/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.view;

import de.jhit.fbs.container.VisualizationEntry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 *
 * @author Jens
 */
public class HtmlPrinter {

    public static final String CONTANT_TAG = "<!-- !!CONTENT!! -->";
    public static final String DATE_TAG = "<--! !!DATE!! -->";
    public static final String TIME_TAG = "<--! !!TIME!! -->";
    public static final String TARGET_TAG = "<--! !!TARGET!! -->";
    public static final String RESON_TAG = "<--! !!REASON!! -->";
    public static final String PERSON_TAG = "<--! !!PERSON!! -->";
    public static final String KMSTART_TAG = "<--! !!KMSTART!! -->";
    public static final String KMFIRM_TAG = "<--! !!KMFIRM!! -->";
    public static final String KMOFFICE_TAG = "<--! !!KMOFFICE!! -->";
    public static final String KMPRIVATE_TAG = "<--! !!KMPRIVATE!! -->";
    public static final String KMEND_TAG = "<--! !!KMEND!! -->";
    public static final String GASAMOUNT_TAG = "<--! !!GASAMOUNT!! -->";
    public static final String GASMONEY_TAG = "<--! !!GASMONEY!! -->";

    public void print(List<VisualizationEntry> entrys) throws UnsupportedEncodingException, IOException {
        StringBuilder template = new StringBuilder();
        StringBuilder content = new StringBuilder();

        File templateFile = new File(getClass().getResource("./zweckform_template.html").getFile());
        File contentFile = new File(getClass().getResource("./zweckform_line.html").getFile());
        Path tmpFile = Paths.get("./tmp.html");

        // read content template
        BufferedReader reader;
        String line;
        for (VisualizationEntry entry : entrys) {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(contentFile)));
            while ((line = reader.readLine()) != null) {
                if (line.contains(DATE_TAG)) {
                    content.append(line.replace(DATE_TAG, entry.getDate()));
                } else if (line.contains(TIME_TAG)) {
                    content.append(line.replace(TIME_TAG, entry.getTime()));
                } else if (line.contains(TARGET_TAG)) {
                    content.append(line.replace(TARGET_TAG, entry.getTarget()));
                } else if (line.contains(RESON_TAG)) {
                    content.append(line.replace(RESON_TAG, entry.getReason()));
                } else if (line.contains(PERSON_TAG)) {
                    content.append(line.replace(PERSON_TAG, entry.getPerson()));
                } else if (line.contains(KMSTART_TAG)) {
                    content.append(line.replace(KMSTART_TAG, entry.getKmStart()));
                } else if (line.contains(KMFIRM_TAG)) {
                    content.append(line.replace(KMFIRM_TAG, entry.getKmFirm()));
                } else if (line.contains(KMOFFICE_TAG)) {
                    content.append(line.replace(KMOFFICE_TAG, entry.getKmOffice()));
                } else if (line.contains(GASMONEY_TAG)) {
                    content.append(line.replace(GASMONEY_TAG, entry.getGasMoney()));
                } else if (line.contains(KMPRIVATE_TAG)) {
                    content.append(line.replace(KMPRIVATE_TAG, entry.getKmPrivate()));
                } else if (line.contains(KMEND_TAG)) {
                    content.append(line.replace(KMEND_TAG, entry.getKmEnd()));
                } else if (line.contains(GASAMOUNT_TAG)) {
                    content.append(line.replace(GASAMOUNT_TAG, entry.getGasAmount()));
                } else {
                    content.append(line);
                }
            }
            reader.close();
            content.append("\n");
        }

        // read file template
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile)));
        while ((line = reader.readLine()) != null) {
            if (line.contains(CONTANT_TAG)) {
                template.append(line.replace(CONTANT_TAG, content.toString()));
            } else {
                template.append(line);
            }
            template.append("\n");
        }
        reader.close();

        // write file
        Files.write(tmpFile, template.toString().getBytes(), StandardOpenOption.CREATE);
    }
}
