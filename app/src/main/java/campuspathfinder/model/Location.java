package campuspathfinder.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

/**
 * Stores a single row of information parsed from campus_buildings_new.tsv
 * 
 * Specification fields:
 * 		@specfield shortName : String // The short name of the building
 * 		@specfield longName : String // The long name of the building
 * 		@specfield coordinate : Coordinate // The coordinates of the building
 * 
 * 
 * Not an ADT
 *
 */
public class Location implements Comparable<Location>{

    @CsvBindByName
	private String shortName;

    @CsvBindByName
	private String longName;

    @CsvCustomBindByName(converter = CoordinateConverter.class)
	private Coordinate location;

    /**
     * Returns the short name of the building.
     * @return the short name of the building
     */
    public String getShortName() {
    	return shortName;
    }
    
    /**
     * Sets the short name of the building to a new given short name
     * @param newName The new short name for the building
     */
    public void setShortName(String newName) {
    	shortName = newName;
    }
    
    /**
     * Returns the long name of the building.
     * @return the long name of the building
     */
    public String getLongName() {
    	return longName;
    }
    
    /**
     * Sets the long name of the building to a new given long name
     * @param newName The new long name for the building
     */
    public void setLongName(String newName) {
    	longName = newName;
    }
    
    /**
     * Returns the location of the building.
     * @return the location of the building
     */
    public Coordinate getLocation() {
    	return location;
    }
    
    /**
     * Sets the location of the building to a new given location
     * @param newLocation The new location of the building
     */
    public void setLocation(Coordinate newLocation) {
    	location = newLocation;
    }

    /**
     * Compares two Locations
     * 
     * @param o The other Location to compare against
     * @return an int indicating order, where a negative int indicates this comes before o, 
     * 		0 if order is equal, a positive int indicates this comes after o
     */
	@Override
	public int compareTo(Location o) {
		if (!this.shortName.equals(o.getShortName())) {
			return this.shortName.compareTo(o.getShortName());
		} 
		double dist1 = this.location.getX() * this.location.getX() +
				this.location.getY() * this.location.getY();
		double dist2 = o.location.getX() * o.location.getX() +
				o.location.getY() * o.location.getY();
		if (dist1 == dist2)
			return 0;
		return (dist2 - dist1 > 0) ? -1 : (dist2 - dist1 < 0) ? 1 : 0;
	}
}
