package campuspathfinder.model.Dijkstras;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import campuspathfinder.model.*;
import campuspathfinder.model.DijkstrasAlgorithm.MalformedDataException;
import campuspathfinder.model.GraphADT.HW5TestDriver.CommandException;


/**
 * A driver that reads test scripts from files and executes the commands
 * described. This can be used for testing Dijkstra's algorithm and your
 * construction of the new Marvel graph.
 *
 * <p>This is not an ADT.</p>
 */
public class HW7TestDriver {

  /**
   * Entry point for a program that reads a test script, either from System.in
   * or from a file provided, and executes the commands found there.
   *
   * @param args List of arguments passed at the command-line.
   * @spec.requires args it not null
   * @spec.effects Interacts with the user by reading from and writing to the
   *     console (System.in and System.out).
   */
  public static void main(String args[]) {
    if (args.length > 1) {
      System.err.println("Usage: java HW7TestDriver [CMD_FILE]");
      System.exit(1);
    }

    try {
      // Create a test driver reading from a file, if a name was provided, or
      // from stdin, if not.
      HW7TestDriver driver = new HW7TestDriver(
          (args.length > 0) ?  new FileReader(new File(args[0])) :
              new InputStreamReader(System.in),
          new OutputStreamWriter(System.out));

      // Run the tests.
      driver.runTests();

    } catch (IOException ex) {
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
  private final Map<String, Graph<String, Double>> graphs = new HashMap<String, Graph<String, Double>>();
  private final PrintWriter output;
  private final BufferedReader input;

  /**
   * Creates a test driver that will read commands from r and write output to w
   *
   * @spec.requires r, w != null
   * @param r Object from which to read the test script.
   * @param w Object to which to write the output of the commands.
   */
  public HW7TestDriver(Reader r, Writer w) {
      input = new BufferedReader(r);
      output = new PrintWriter(w);
  }

  /**
   * Executes the commands found in the input.
   * 
   * @spec.modifies this
   * @spec.effects Executes the commands read from the input and writes
   *      results to the output
   * @throws IOException if the input or output sources encounters I/O errors
   */
  public void runTests() throws IOException {
	  String inputLine;
      while ((inputLine = input.readLine()) != null) {
          if ((inputLine.trim().length() == 0) ||
              (inputLine.charAt(0) == '#')) {
              // echo blank and comment lines
              output.println(inputLine);
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

                  executeCommand(command, arguments);
              }
          }
          output.flush();
      }
  }
  
  private void executeCommand(String command, List<String> arguments) {
      try {
    	  if (command.equals("CreateGraph")) {
              createGraph(arguments);
          } else if (command.equals("AddNode")) {
              addNode(arguments);
          } else if (command.equals("AddEdge")) {
              addEdge(arguments);
          } else if (command.equals("ListNodes")) {
              listNodes(arguments);
          } else if (command.equals("ListChildren")) {
              listChildren(arguments);
          } else if (command.equals("LoadGraph")) {
              loadGraph(arguments);
          } else if (command.equals("FindPath")) {
              findPath(arguments);
          } else {
              output.println("Unrecognized command: " + command);
          }
      } catch (Exception e) {
          output.println("Exception: " + e.toString());
      }
  }

  private void createGraph(List<String> arguments) {
      if (arguments.size() != 1) {
          throw new CommandException("Bad arguments to CreateGraph: " + arguments);
      }

      String graphName = arguments.get(0);
      createGraph(graphName);
  }

  private void createGraph(String graphName) {

      graphs.put(graphName, new Graph<String, Double>());
      output.println("created graph " + graphName);
  }

  private void addNode(List<String> arguments) {
      if (arguments.size() != 2) {
          throw new CommandException("Bad arguments to addNode: " + arguments);
      }

      String graphName = arguments.get(0);
      String nodeName = arguments.get(1);

      addNode(graphName, nodeName);
  }

  private void addNode(String graphName, String nodeName) {

      Graph<String, Double> graph = graphs.get(graphName);
      graph.addNode(new GraphNode<String, Double>(nodeName));
      output.println("added node " + nodeName + " to " + graphName);
  }

  private void addEdge(List<String> arguments) {
      if (arguments.size() != 4) {
          throw new CommandException("Bad arguments to addEdge: " + arguments);
      }

      String graphName = arguments.get(0);
      String parentName = arguments.get(1);
      String childName = arguments.get(2);
      Double edgeLabel = Double.parseDouble(arguments.get(3));

      addEdge(graphName, parentName, childName, edgeLabel);
  }

  private void addEdge(String graphName, String parentName, String childName,
		  Double edgeLabel) {

      Graph<String, Double> graph = graphs.get(graphName);
      GraphNode<String, Double> parentNode = new GraphNode<String, Double>("data");
      GraphNode<String, Double> childNode = new GraphNode<String, Double>("data");
      Set<GraphNode<String, Double>> nodes = graph.getNodes();
      for (GraphNode<String, Double> node : nodes) {
      	if (node.getData().equals(parentName)) {
      		parentNode = node;
      	}
      }
      for (GraphNode<String, Double> node : nodes) {
      	if (node.getData().equals(childName)) {
      		childNode = node;
      	}
      }
      GraphEdge<String, Double> edge = new GraphEdge<String, Double>(edgeLabel, childNode);
      parentNode.addEdge(edge);
      String label = String.format("%.3f", edgeLabel);
      output.println("added edge " + label + " from " 
      		+ parentName + " to " + childName + " in " + graphName);
  }

  private void listNodes(List<String> arguments) {
      if (arguments.size() != 1) {
          throw new CommandException("Bad arguments to listNodes: " + arguments);
      }

      String graphName = arguments.get(0);
      listNodes(graphName);
  }

  private void listNodes(String graphName) {

      Graph<String, Double> graph = graphs.get(graphName);
      Set<GraphNode<String, Double>> list = graph.getSortedNodes();
  	output.print(graphName + " contains:");
  	for (GraphNode<String, Double> node : list) {
  		output.print(" " + node.getData());
  	}
  	output.println();
  }

  private void listChildren(List<String> arguments) {
      if (arguments.size() != 2) {
          throw new CommandException("Bad arguments to listChildren: " + arguments);
      }

      String graphName = arguments.get(0);
      String parentName = arguments.get(1);
      listChildren(graphName, parentName);
  }

  private void listChildren(String graphName, String parentName) {
      output.print("the children of " + parentName + " in " + graphName + " are:");
      Graph<String, Double> graph = graphs.get(graphName);
      GraphNode<String, Double> parent = new GraphNode<String, Double>("data");
      for (GraphNode<String, Double> node : graph.getNodes()) {
      	if (node.getData().equals(parentName)) {
      		parent = node;
      	}
      }
      Set<GraphNode<String, Double>> children = parent.getChildren();
      for (GraphNode<String, Double> child : children) {
      	for(GraphEdge<String, Double> edge : parent.getEdgesFromNode(child)) {
            String label = String.format("%.3f", edge.getLabel());
      		output.print(" " + child.getData() + "(" 
      				+ label + ")");
      	}
      }
      output.println();
  }
  
  private void loadGraph(List<String> arguments) throws MalformedDataException {
      if (arguments.size() != 2) {
          throw new CommandException("Bad arguments to LoadGraph: " + arguments);
      }

      String graphName = arguments.get(0);
      String fileName = arguments.get(1);
      loadGraph(graphName, fileName);
  }

  private void loadGraph(String graphName, String fileName) throws MalformedDataException {
	  String pathName = "../cse331-19wi-tane1999/src/test/"
	  		+ "resources/hw7/data/" + fileName;
      Graph<String, Double> graph = DijkstrasAlgorithm.buildGraph(pathName);
      graphs.put(graphName, graph);
      output.println("loaded graph " + graphName);
  }
  
  private void findPath(List<String> arguments) {
      if (arguments.size() != 3) {
          throw new CommandException("Bad arguments to FindPath: " + arguments);
      }

      String graphName = arguments.get(0);
      String src = arguments.get(1);
      String dest = arguments.get(2);
      findPath(graphName, src, dest);
  }

  private void findPath(String graphName, String src, String dest) {
	  src = src.replace('_', ' ');
	  dest = dest.replace('_', ' ');
      Graph<String, Double> graph = graphs.get(graphName);
      if (graph.getNode(src) == null) {
    	  output.println("unknown character " + src);
      } else if (graph.getNode(dest) == null) {
    	  output.println("unknown character " + dest);
      } else {
	      List<GraphEdge<String, Double>> path = DijkstrasAlgorithm.shortestPath(graph, src, dest);
	      output.println("path from " + src + " to " + dest + ":");
	      if (path == null) {
	    	  output.println("no path found");
	      } else {
		      GraphNode<String, Double> currChar = graph.getNode(src);
		      double cost = 0;
		      for (GraphEdge<String, Double> edge : path) {
		          String label = String.format("%.3f", edge.getLabel());
		    	  output.println(currChar.getData() + " to " 
		    			  + edge.getNode().getData() + " with weight " + label);
		    	  cost += edge.getLabel();
		    	  currChar = edge.getNode();
		      }
		      String totalCost = String.format("%.3f", cost);
		      output.println("total cost: " + totalCost);
	      }
      }
  }
}
