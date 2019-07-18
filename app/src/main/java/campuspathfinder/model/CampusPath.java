package campuspathfinder.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

/**
 * Stores a single row from campus_paths.tsv. The names of the methods are
 * chosen to match the names in the first line of that file.
 */
public class CampusPath {

    // Not an ADT

    @CsvCustomBindByName(converter = CoordinateConverter.class)
    private Coordinate origin;

    @CsvCustomBindByName(converter = CoordinateConverter.class)
    private Coordinate destination;

    @CsvBindByName
    private double distance;

    /**
     * Returns the starting point of the path segment.
     * @return the starting point of the path segment
     */
    public Coordinate getOrigin() {
        return origin;
    }

    /**
     * Sets the starting point of the path segment.
     * @param origin the new starting point of the path segment
     */
    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }

    /**
     * Returns the ending point of the path segment.
     * @return the ending point of the path segment
     */
    public Coordinate getDestination() {
        return destination;
    }

    /**
     * Sets the ending point of the path segment.
     * @param destination the new ending point of the path segment
     */
    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }

    /**
     * Returns the distance between the starting and ending points.
     * @return the distance of the path segment
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Sets the distance between the starting and ending points.
     * @param distance the new distance of the path segment
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }
}
