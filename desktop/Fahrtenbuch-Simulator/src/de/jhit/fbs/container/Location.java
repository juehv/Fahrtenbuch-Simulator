/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.jhit.fbs.container;

import java.util.Objects;

/**
 *
 * @author Jens
 */
public class Location {
    // fast forward version

    public String point;

    public Location(String point) {
        this.point = point;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.point);
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
        final Location other = (Location) obj;
        if (!this.point.equalsIgnoreCase(other.point)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return point;
    }
}
