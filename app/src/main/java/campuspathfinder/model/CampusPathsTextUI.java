package campuspathfinder.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * The client side view of a program to find view buildings in a campus and find the shortest
 * path between two buildings.
 *
 *
 * Not an ADT
 */
public class CampusPathsTextUI {

	/**
	 * Runs the program
	 * @param args Extra arguments to the program
	 */
	public static void main(String[] args) {
		CampusPathsTextUI finder;
		try {
			if (args.length == 0) {
				finder = new CampusPathsTextUI(new InputStreamReader(System.in),
	                                       new OutputStreamWriter(System.out));
				finder.runFinder();
			}
		} catch (IOException e) {
			return;
		}
	}
	
	//reads input
	private final BufferedReader input;
	
	//writes output
	private final PrintWriter output;
	
	//the campus
	private Campus campus;
	
	//file path to read from
	private final String FILE_PATH = "../cse331-19wi-tane1999/src/main/resources/hw8/";

	/**
	 * Constructs a new CampusPathsTextUI object
	 * 
	 * @param r The Reader to read from
	 * @param w The Writer to write to
	 * @throws IOException If there are any I/O problems reading the files
	 * @spec.effects Constructs a new CampusPathsTextUI object
	 */
	public CampusPathsTextUI(Reader r, Writer w) throws IOException {
		this(r, w, "campus_buildings_new.tsv", "campus_paths.tsv");
	}
	
	/**
	 * Constructs a new CampusPathsTextUI object
	 * 
	 * @param r The Reader to read from
	 * @param w The Writer to write to
	 * @param building The building file to read from
	 * @param path The path file to read from
	 * @throws IOException If there are any I/O problems reading the files
	 * @spec.effects Constructs a new CampusPathsTextUI object
	 */
	public CampusPathsTextUI(Reader r, Writer w, String building, String path) throws IOException {
		input = new BufferedReader(r);
		output = new PrintWriter(w);
		campus = new Campus();
		campus.loadCampus(FILE_PATH + building, FILE_PATH + path);
	}
	
	/**
	 * Runs the program by continuously reading inputs until the quit command
	 * 
	 * @throws IOException If there are any I/O problems
	 */
	public void runFinder() throws IOException {
		String inputLine;
		printMenu();
		output.println();
		output.flush();
		output.print("Enter an option ('m' to see the menu): ");
		output.flush();
		// {{ Inv: the command contained in inputLine has been read and run }}
        while ((inputLine = input.readLine()) != null) {
            if ((inputLine.trim().length() == 0) ||
                (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
                output.flush();
            }
            else
            {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }
                    if (command.equals("q")) {
                    	return;
                    } else {
                    	executeCommand(command, arguments);
                    }
                }
            }
        }
	}
	
	/**
	 * Executes the provided command
	 * 
	 * @param command The command to be executed
	 * @param arguments Extra arguments following the commmand
	 * @throws IOException If there are any I/O problems
	 */
	private void executeCommand(String command, List<String> arguments) throws IOException {
		if (arguments.size() > 0) {
			output.println("Unknown option");
			output.flush();
		} else {
			if (command.equals("m")) {
				printMenu();
			} else if (command.equals("r")) {
				route();
			} else if (command.equals("b")) {
				list();
			} else {
				output.println("Unknown option");
				output.flush();
			}
		}
		output.println();
		output.flush();
		output.print("Enter an option ('m' to see the menu): ");
		output.flush();
	}
	
	/**
	 * Prints the menu for commands
	 */
	private void printMenu() {
		output.println("Menu:");
		output.flush();
		output.println("\tr to find a route");
		output.flush();
		output.println("\tb to see a list of all buildings");
		output.flush();
		output.println("\tq to quit");
		output.flush();
	}
	
	/**
	 * Prompts the user to enter buildings for path finding
	 */
	private void route() {
		try {
	    	output.print("Abbreviated name of starting building: ");
	    	output.flush();
	    	String b1 = input.readLine();
	    	output.print("Abbreviated name of ending building: ");
	    	output.flush();
	    	String b2 = input.readLine();
	    	String long1 = campus.getLongName(b1);
	    	String long2 = campus.getLongName(b2);
	    	if (long1 == null || long2 == null) {
	    		if (long1 == null) {
		    		output.println("Unknown building: " + b1);
		    		output.flush();
	    		}
	    		if (long2 == null) {
		    		output.println("Unknown building: " + b2);
		    		output.flush();
	    		}
	    		return;
	    	}
	    	output.println("Path from " + campus.getLongName(b1) + 
	    			" to " + campus.getLongName(b2) + ":");
	    	output.flush();
	    	pathFinder(b1, b2);
		} catch (IOException e) {
			output.println("Unknown option");
			output.flush();
		}
	}
	
	/**
	 * Finds and prints the path between two given buildings
	 * 
	 * @param b1 The abbreviated name of the starting building
	 * @param b2 The abbreviated name of the ending building
	 */
	private void pathFinder(String b1, String b2) {
		List<double[]> path = campus.findPath(b1, b2);
		Location startingLoc = campus.getLocation(b1);
		double[] curr = {startingLoc.getLocation().getX(), startingLoc.getLocation().getY(), 0.0};
		Iterator<double[]> itr = path.iterator();
		double totalDist = 0;
		// {{ Inv: for each double[] in itr that we have seen so far, it has been printed to 
		//		the output as path information in the appropriate syntax, and totalDist
		//		has been incremented by the appropriate amount }}
		while (itr.hasNext()) {
			double[] next = itr.next();
			double distance = next[2];
			totalDist += distance;
			String dir = getDirection(curr[0], curr[1], next[0], next[1]);
			output.println("\tWalk " + String.format("%.0f", distance) + 
					" feet " + dir + " to " + String.format("(%.0f, %.0f)", next[0],
							next[1]));
			output.flush();
			curr = next;
		}
		output.println("Total distance: " + String.format("%.0f", totalDist) + " feet");
		output.flush();
	}
	
	/**
	 * Returns the direction from the given starting location to the ending location
	 * 
	 * @param startingX The X coordinate of the starting location
	 * @param startingY The Y coordinate of the starting location
	 * @param endingX The X coordinate of the ending location
	 * @param endingY The Y coordinate of the ending location
	 * @return A letter indicating the compass direction 
	 */
	private String getDirection(double startingX, double startingY, 
			double endingX, double endingY) {
		double coord1 = endingX - startingX;
		double coord2 = endingY - startingY;
		double theta = Math.atan2(coord2, coord1) * -1;
		if (theta <= Math.PI / 8 && theta >= -1 * Math.PI / 8) {
			return "E";
		}
		if (theta < 3 * Math.PI / 8 && theta > Math.PI / 8) {
			return "NE";
		}
		if (theta < 7 * Math.PI / 8 && theta > 5 * Math.PI / 8) {
			return "NW";
		}
		if (theta < -5 * Math.PI / 8 && theta > -7 * Math.PI / 8) {
			return "SW";
		}
		if (theta < -1 * Math.PI / 8 && theta > -3 * Math.PI / 8) {
			return "SE";
		}
		if (theta <= -3 * Math.PI / 8 && theta >= -5 * Math.PI / 8) {
			return "S";
		}
		if (theta <= 5 * Math.PI / 8 && theta >= 3 * Math.PI / 8) {
			return "N";
		}
		return "W";
	}
	
	/**
	 * Lists the buildings in the campus, in the form abbreviated name: long name
	 */
	private void list() {
		Map<String, String> buildings = campus.getBuildings();
		output.println("Buildings:");
		output.flush();
		for (String abbrev : buildings.keySet()) {
			output.println("\t" + abbrev + ": " + buildings.get(abbrev));
			output.flush();
		}
	}
}
