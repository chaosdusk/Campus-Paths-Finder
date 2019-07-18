package campuspathfinder.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents a campus in a Graph, with campus buildings as GraphNodes and campus paths as GraphEdges
 * Paths are directed and labeled with the distance between its origin and destination.
 * Buildings have two names: a short and a long name.
 * 
 * A Campus is initialized from a file containing buildings and another containing paths, both of which
 * should match the shape defined by Location and CampusPath, respectively.
 * 
 * A Campus is mutable, but new buildings and paths can only be added by modifying the files that the campus
 * is loaded from and then calling loadCampus()
 * 
 * Specification Fields:
 * 		@specfield campus : Graph&lt;CampusBuilding, Double&gt; // A Graph representing the campus
 * 		@specfield campusBuildings : Map&lt;String, Location&gt; // A Map from the names of buildings to
 * 		themselves
 * 		@specfield locations : Map&lt;Coordinate, Location&gt; // A Map from Coordinate of buildings to
 * 		themselves
 * 
 * 
 */
public class Campus {

	/*
	 * Abstract Function: 
	 * A Campus is a mathematical graph of buildings as nodes and paths as edges.
	 * If there are no nodes in 'campus,' then the Campus is empty.
	 * 
	 * Representation Invariant:
	 * campusBuildings != null && campus != null && locations != null &&
	 * for (0 < i < campus.size(), campus.get(i) != null
	 * 
	 * In other words, no buildings in the campus is null
	 * 
	 * 
	 */
	
	//the campus 
	private Graph<Location, Double> campus;
	
	//maps the name of buildings to the respective Location
	private Map<String, Location> campusBuildings;
	
	//maps
	private Map<Coordinate, Location> locations;
		
	/**
	 * @spec.effects Constructs a new empty Campus object
	 */
	public Campus() {
		campus = new Graph<Location, Double>();
		campusBuildings = new HashMap<String, Location>();
		locations = new HashMap<Coordinate, Location>();
	}
	
	/**
	 * Loads the campus from given files
	 * 
	 * @param buildingFileName The file name of the buildings
	 * @param pathFileName The file name of the paths
	 * @throws IOException if any I/O error occurs reading the file
	 * @throws IllegalArgumentException if buildingFileName or pathFileName is null
	 * @spec.effects Loads the campus from given building and path files
	 */
	public void loadCampus(String buildingFileName, String pathFileName) throws IOException {
		checkRep();
		if (buildingFileName == null || pathFileName == null) {
			throw new IllegalArgumentException("a file path is null");
		}
		campus = new Graph<Location, Double>();
		initializeNodes(buildingFileName);
		initializePaths(pathFileName);
	}
	
	/**
	 * Loads the campus from given files
	 * 
	 * @param buildingFile The InputStream file of the buildings
	 * @param pathFile The InputStream file of the paths
	 * @throws IOException if any I/O error occurs reading the file
	 * @throws IllegalArgumentException if buildingFile or pathFile is null
	 * @spec.effects Loads the campus from given building and path files
	 */
	public void loadCampus(InputStream buildingFile, InputStream pathFile) throws IOException {
		checkRep();
		if (buildingFile == null || pathFile == null) {
			throw new IllegalArgumentException("a file path is null");
		}
		campus = new Graph<Location, Double>();
		initializeNodes(buildingFile);
		initializePaths(pathFile);
	}
	
	/**
	 * Loads the campus with buildings from given file
	 * 
	 * @param fileName The file name of the buildings
	 * @throws IOException if any I/O error occurs reading the file
	 * @spec.effects Loads the campus with given buildings
	 */
	private void initializeNodes(String fileName) throws IOException {
		checkRep();
		List<Location> buildings = AndroidParser.parseBuildingData(new FileInputStream(fileName));
		// {{ Inv: for each building in buildings that we have seen so far, it has been 
		//		added to campus, campusBuildings, and locations in the appropriate forms }}
		for (Location building : buildings) {
			GraphNode<Location, Double> node = new GraphNode<Location, Double>(building);
			campus.addNode(node);
			campusBuildings.put(building.getShortName(), building);
			locations.put(building.getLocation(), building);
		}
	}
	
	/**
	 * Loads the campus with buildings from given file
	 * 
	 * @param file The InputStream file of the buildings
	 * @throws IOException if any I/O error occurs reading the file
	 * @spec.effects Loads the campus with given buildings
	 */
	private void initializeNodes(InputStream file) throws IOException {
		checkRep();
		List<Location> buildings = AndroidParser.parseBuildingData(file);
		// {{ Inv: for each building in buildings that we have seen so far, it has been 
		//		added to campus, campusBuildings, and locations in the appropriate forms }}
		for (Location building : buildings) {
			GraphNode<Location, Double> node = new GraphNode<Location, Double>(building);
			campus.addNode(node);
			campusBuildings.put(building.getShortName(), building);
			locations.put(building.getLocation(), building);
		}
	}
	
	/**
	 * Loads the campus with buildings from given file
	 * 
	 * @param fileName The file name of the paths
	 * @throws IOException If any I/O error occurs reading the file
	 * @spec.effects Loads the campus with given paths
	 */
	private void initializePaths(String fileName) throws IOException {
		checkRep();
		List<CampusPath> paths = AndroidParser.parsePathData(new FileInputStream(fileName));
		// {{ Inv: for each path in paths that we have seen so far, its starting and ending 
		//		points have been added as GraphNodes to campus and a GraphEdge with the distance
		//		as its label as been added to the starting node, with it pointing to the 
		//		end node }}
		for (CampusPath path : paths) {
			GraphNode<Location, Double> start = campus.getNode(locations.get(path.getOrigin()));
			GraphNode<Location, Double> end = campus.getNode(locations.get(path.getDestination()));
			if (start == null) {
				Location st = new Location();
				st.setShortName("");
				st.setLongName("");
				st.setLocation(path.getOrigin());
				start = new GraphNode<Location, Double>(st);
				campus.addNode(start);
				locations.put(path.getOrigin(), st);
			}
			if (end == null) {
				Location ed = new Location();
				ed.setShortName("");
				ed.setLongName("");
				ed.setLocation(path.getDestination());
				end = new GraphNode<Location, Double>(ed);
				campus.addNode(end);
				locations.put(path.getDestination(), ed);
			}
			campus.addEdge(start, end, path.getDistance());
		}
	}
	
	/**
	 * Loads the campus with buildings from given file
	 * 
	 * @param file The InputStream file of the paths
	 * @throws IOException If any I/O error occurs reading the file
	 * @spec.effects Loads the campus with given paths
	 */
	private void initializePaths(InputStream file) throws IOException {
		checkRep();
		List<CampusPath> paths = AndroidParser.parsePathData(file);
		// {{ Inv: for each path in paths that we have seen so far, its starting and ending 
		//		points have been added as GraphNodes to campus and a GraphEdge with the distance
		//		as its label as been added to the starting node, with it pointing to the 
		//		end node }}
		for (CampusPath path : paths) {
			GraphNode<Location, Double> start = campus.getNode(locations.get(path.getOrigin()));
			GraphNode<Location, Double> end = campus.getNode(locations.get(path.getDestination()));
			if (start == null) {
				Location st = new Location();
				st.setShortName("");
				st.setLongName("");
				st.setLocation(path.getOrigin());
				start = new GraphNode<Location, Double>(st);
				campus.addNode(start);
				locations.put(path.getOrigin(), st);
			}
			if (end == null) {
				Location ed = new Location();
				ed.setShortName("");
				ed.setLongName("");
				ed.setLocation(path.getDestination());
				end = new GraphNode<Location, Double>(ed);
				campus.addNode(end);
				locations.put(path.getDestination(), ed);
			}
			campus.addEdge(start, end, path.getDistance());
		}
	}
	
	/**
	 * Finds the path between a src building and a dest building
	 * 
	 * @param src The starting building
	 * @param dest The destination building
	 * @spec.requires src and dest are valid buildings
	 * @return a List&lt;double[]&gt; containing the path, where each double[] represents an 
	 * 		intermediate location or the destination, and within each double[], the 0th index
	 * 		represents the X coordinate of the location, the 1st index represents the Y
	 * 		coordinate of the location, and the 2nd index represents the distance between the
	 * 		previous intermediate location to this
	 */
	public List<double[]> findPath(String src, String dest) {
		checkRep();
		List<double[]> path = new ArrayList<double[]>();
		Location st = campusBuildings.get(src);
		Location ed = campusBuildings.get(dest);
		List<GraphEdge<Location, Double>> route = DijkstrasAlgorithm.shortestPath(campus, st, ed);
		// {{ Inv: for each edge in route that we have seen so far, it has been added to path
		//		as a new double[] with the 0th index as the X coordinate of the destination, 
		//		the 1st index as the Y coordinate of the destination, and the 2nd index as the
		//		distance to the destination }}
		for (GraphEdge<Location, Double> edge : route) {
			double[] data = new double[3];
			Location b = edge.getNode().getData();
			data[0] = b.getLocation().getX();
			data[1] = b.getLocation().getY();
			data[2] = edge.getLabel();
			path.add(data);
		}
		return path;
	}
	
	/**
	 * Gets the buildings in the campus, sorted alphabetically by its abbreviated name
	 * 
	 * @return a Map&lt;String, String&gt; of buildings, where abbreviated names are mapped
	 * 		to long names, sorted alphabetically by abbreviated names
	 */
	public Map<String, String> getBuildings() {
		checkRep();
		Map<String, String> buildings = new TreeMap<String, String>();
		for (Location b: campusBuildings.values()) {
			buildings.put(b.getShortName(), b.getLongName());
		}
		return buildings;
	}
	
	/**
	 * Gets the long name of a building from its abbreviated name
	 * 
	 * @param shortName The abbreviated name of the building
	 * @return The long name of the building, or null if the building does not exist
	 */
	public String getLongName(String shortName) {
		if (!campusBuildings.containsKey(shortName)) {
			return null;
		}
		return campusBuildings.get(shortName).getLongName();
	}
	
	/**
	 * Gets the Location of a building from its abbreviated name
	 * 
	 * @param shortName The abbreviated name of the building
	 * @return The Location of the building, or null if the building does not exist
	 */
	public Location getLocation(String shortName) {
		return campusBuildings.get(shortName);
	}
	
	/**
	 * Checks the representation of this
	 */
	private void checkRep() {
		assert(campus != null) : "campus is null";
		assert(campusBuildings != null) : "campusBuildings is null";
		assert(locations != null) : "locations is null";
	}
}
