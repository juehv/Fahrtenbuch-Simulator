/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import com.csvreader.CsvWriter;
import de.jhit.fbs.smartcontainer.RouteTimeTable;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Jens
 */
public class Route {

    public Marker marker = new Marker();
    public int appearanceCount = 1;
    public Location start;
    public Location end;
    public List<Location> detours;
    public int km;
    public RouteTimeTable typicalTimes;

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Route other = (Route) obj;
        if (!Objects.equals(this.start, other.start)
                && !Objects.equals(this.start, other.end)) {
            return false;
        }
        if (!Objects.equals(this.end, other.end)
                && !Objects.equals(this.end, other.start)) {
            return false;
        }
        if (this.detours.size() != other.detours.size()) {
            return false;
        }
        for (Location item : other.detours) {
            if (!this.detours.contains(item)) {
                return false;
            }
        }
        return true;
    }

    public CsvWriter toCsvQuestionLine(CsvWriter writer) throws IOException {
        writer.write(marker.toCsvString());
        writer.write(start.toString());
        writer.write(end.toString());
        if (detours != null && !detours.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Location item : detours) {
                sb.append(item.toString()).append(Constants.DETOURS_DELIMITER);
            }
            writer.write(sb.toString());
        } else {
            writer.write("");
        }
        writer.write(String.valueOf(km));

        return typicalTimes.toCsvQuestionLine(writer);
    }

    @Override
    public String toString() {
        return "Route{" + "marker=" + marker.toCsvString() + ", start=" + start + ", end=" + end + ", detours=" + detours + ", km=" + km + ", typicalTimes=" + typicalTimes + '}';
    }

    public static Comparator<Route> getComparator() {
        return new Comparator<Route>() {
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
        };
    }

}
