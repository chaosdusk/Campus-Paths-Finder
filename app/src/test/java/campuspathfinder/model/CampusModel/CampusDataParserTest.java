package campuspathfinder.model.CampusModel;

import static org.junit.Assert.assertEquals;
import campuspathfinder.model.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;


public class CampusDataParserTest {


	// parsePathData() tests
	
	@Test
	public void pathSmallTest() throws IOException {
		List<CampusPath> paths = CampusDataParser.parsePathData("../cse331-19wi-tane1999/src/main/"
				+ "resources/hw8/campus_paths_small.tsv");
		assert(paths.size() == 3);
		Iterator<CampusPath> itr = paths.iterator();
		CampusPath curr = itr.next();
		assert(curr.getOrigin().getX() == 3.0);
		assert(curr.getOrigin().getY() == 3.1);
		assert(curr.getDestination().getX() == 4.4);
		assert(curr.getDestination().getY() == 4.5);
		assert(curr.getDistance() == 12);
		curr = itr.next();
		assert(curr.getOrigin().getX() == 7.0);
		assert(curr.getOrigin().getY() == 7.1);
		assert(curr.getDestination().getX() == 120.123);
		assert(curr.getDestination().getY() == 120.124);
		assert(curr.getDistance() == 1);
		curr = itr.next();
		assert(curr.getOrigin().getX() == 4.0);
		assert(curr.getOrigin().getY() == 4.0);
		assert(curr.getDestination().getX() == 3);
		assert(curr.getDestination().getY() == 3.333);
		assert(curr.getDistance() == 1.1);
		
	}
	
	@Test
	public void pathBigInitializationTest() throws IOException {
		List<CampusPath> paths = CampusDataParser.parsePathData("../cse331-19wi-tane1999/src/main/"
				+ "resources/hw8/campus_paths.tsv");
		assert(paths.size() == 5546);
	}
	
	@Test
	public void buildingSmallTest() throws IOException {
		List<Location> buildings = CampusDataParser.parseBuildingData("../cse331-19wi-tane1999/src/main/"
				+ "resources/hw8/campus_buildings_small.tsv");
		assert(buildings.size() == 3);
		Iterator<Location> itr = buildings.iterator();
		Location curr = itr.next();
		assertEquals("B1", curr.getShortName());
		assertEquals("Building1", curr.getLongName());
		assert(curr.getLocation().getX() == 1.1);
		assert(curr.getLocation().getY() == 1.1);
		curr = itr.next();
		assertEquals("B2", curr.getShortName());
		assertEquals("Building2", curr.getLongName());
		assert(curr.getLocation().getX() == 10.0);
		assert(curr.getLocation().getY() == 10.0);
		curr = itr.next();
		assertEquals("B3", curr.getShortName());
		assertEquals("Building3", curr.getLongName());
		assert(curr.getLocation().getX() == 100.1);
		assert(curr.getLocation().getY() == 0.0);
	}
	
	@Test
	public void buildingBigInitializationTest() throws IOException {
		List<Location> buildings = CampusDataParser.parseBuildingData("../cse331-19wi-tane1999/src/main/"
				+ "resources/hw8/campus_buildings_new.tsv");
		assert(buildings.size() == 51);
	}
}
