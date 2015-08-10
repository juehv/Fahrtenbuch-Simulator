/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

/**
 *
 * @author Jens
 */
public class VisualizationEntry {

    public String date = "&nbsp;";
    public String time = "&nbsp;";
    public String target = "&nbsp;";
    public String reason = "&nbsp;";
    public String person = "&nbsp;";
    public String kmStart = "&nbsp;";
    public String kmEnd = "&nbsp;";
    public String kmPrivate = "&nbsp;";
    public String kmFirm = "&nbsp;";
    public String kmOffice = "&nbsp;";
    public String gasAmount = "&nbsp;";
    @Deprecated
    public String gasMoney = "&nbsp;";

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTarget() {
        return target;
    }

    public String getReason() {
        return reason;
    }

    public String getPerson() {
        return person;
    }

    public String getKmStart() {
        return kmStart;
    }

    public String getKmEnd() {
        return kmEnd;
    }

    public String getKmPrivate() {
        return kmPrivate;
    }

    public String getKmFirm() {
        return kmFirm;
    }

    public String getKmOffice() {
        return kmOffice;
    }

    public String getGasAmount() {
        return gasAmount;
    }

    @Deprecated
    public String getGasMoney() {
        return gasMoney;
    }

}
