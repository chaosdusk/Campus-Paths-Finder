package campuspathfinder.model.CampusModel;

import static org.junit.Assert.assertEquals;
import campuspathfinder.model.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class CampusTest {

	private final String FILE_PATH = "../cse331-19wi-tane1999/src/main/resources/hw8/";

	@Test
	public void constructorTest() {
		Campus campus = new Campus();
	}
	
	@Test
	public void loadSmallMapTest() throws IOException {
		Campus campus = new Campus();
		campus.loadCampus(FILE_PATH + "campus_buildings_small.tsv", FILE_PATH + "campus_paths_small2.tsv");
		assert(campus.getBuildings().size() == 3);
		Iterator<String> itr = campus.getBuildings().keySet().iterator();
		assertEquals("B1", itr.next());
		assertEquals("B2", itr.next());
		assertEquals("B3", itr.next());
	}
	
	@Test
	public void loadBigMapTest() throws IOException {
		Campus campus = new Campus();
		campus.loadCampus(FILE_PATH + "campus_buildings_new.tsv", FILE_PATH + "campus_paths.tsv");
		assert(campus.getBuildings().size() == 51);
		
	}
	
	@Test
	public void findSmallPathTest() throws IOException {
		Campus campus = new Campus();
		campus.loadCampus(FILE_PATH + "campus_buildings_small.tsv", FILE_PATH + "campus_paths_small2.tsv");
		List<double[]> path = campus.findPath("B1", "B3");
		assert(path.size() == 2);
		Iterator<double[]> itr = path.iterator();
		double[] curr = itr.next();
		assert(curr[0] == 10.0);
		assert(curr[1] == 10.0);
		curr = itr.next();
		assert(curr[0] == 100.1);
		assert(curr[1] == 0.0);
		
	}
	
}
