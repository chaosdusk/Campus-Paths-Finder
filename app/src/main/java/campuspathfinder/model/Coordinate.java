package campuspathfinder.model;

import java.util.Objects;

//
// NOTE: DO NOT CHANGE THIS FILE
//

/** Represents an (x,y) coordinate. */
public class Coordinate {

    // Not an ADT

    private double x, y;

    /** Creates the coordinate (0,0) */
    public Coordinate() {
      x = y = 0;
    }

    /**
     * Creates a coordinate with the given parts.
     * @param x the X part of the coordinate
     * @param y the Y part of the coordinate
     */
    public Coordinate(double x, double y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Returns the X part of the coordinate.
     * @return the X part of the coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X part of the coordinate.
     * @param x the X part of the coordinate.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the Y part of the coordinate.
     * @return the Y part of the coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y part of the coordinate.
     * @param y the Y part of the coordinate.
     */
    public void setY(double y) {
        this.y = y;
    }

    @Override public String toString() {
      return String.format("(%.5f,%.5f)", x, y);  // useful for debugging
    }

    @Override public boolean equals(Object o) {
      if (!(o instanceof Coordinate))
        return false;
      Coordinate c = (Coordinate) o;
      return x == c.x && y == c.y;
    }

    @Override public int hashCode() {
      return Objects.hash(x, y);
    }
}
