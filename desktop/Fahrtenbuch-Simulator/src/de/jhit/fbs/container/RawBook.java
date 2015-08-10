/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jens
 */
public class RawBook {

    public List<DataEntry> entrys;
    public Map<String, String> shortcuts;

    public List<Route> getRoutes() {
        List<Route> routes = new ArrayList<>();
        for (DataEntry entry : entrys) {
            routes.add(entry.route);
        }
        return routes;
    }
}
