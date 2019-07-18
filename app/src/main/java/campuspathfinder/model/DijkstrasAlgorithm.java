package campuspathfinder.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.PriorityQueue;

/**
 * Program that allows users to find the least cost path between characters in
 * the Marvel graph. The user is prompted to enter the name of the file
 * containing information about characters and comics along with the names of
 * two characters in question. If both characters exist, it will then display a
 * least cost path between the two characters.
 *
 * <p>This is not an ADT.</p>
 */
public class DijkstrasAlgorithm<K extends Comparable<K>> {
	
	/**
	 * Comparator that allows comparison between two paths by weighting, and in the event of 
	 * a tie, compares lexicographically by the destination nodes
	 *
	 * @param <K> The key/name/data of a GraphNode/the data that a GraphNode holds
	 */
	static class PathComparator<K extends Comparable<K>> 
		implements Comparator<ArrayList<GraphEdge<K, Double>>> {

		/**
		 * Compares the two given ArrayLists
		 * 
		 * @param o1 the ArrayList to be compared
		 * @param o2 the ArrayList to be compared
		 * @return the value 0 if o1 and o2 have the same weighting and destination node
		 * 		a value less than 0 if o1 has smaller weighting than o2, or they have the same
		 * 		weighting but o1's destination node is lexicographically less than o2's 
		 * 		destination node
		 * 		a value greater than 0 if o1 has greater weighting than o2, or they have the same
		 * 		weighting but o1's destination node is lexicographically greater than o2's
		 * 		destination node
		 */
		@Override
		public int compare(ArrayList<GraphEdge<K, Double>> o1, ArrayList<GraphEdge<K, Double>> o2) {
			double cost1 = 0.0;
			double cost2 = 0.0;
			for (GraphEdge<K, Double> edge : o1) {
				cost1 += edge.getLabel();
			}
			for (GraphEdge<K, Double> edge : o2) {
				cost2 += edge.getLabel();
			}
			if (cost1 != cost2) {
				return (cost1 > cost2) ? 1 : -1;
			}
			return o1.get(o1.size() - 1).getNode().compareTo(o2.get(o2.size() - 1).getNode());
		}
	}

	/**
	 * Returns the shortest path from the given source character to the given 
	 * destination character via edges in the given graph.
	 * 
	 * @param <K> The key/name/data of a GraphNode or that a GraphNode holds
	 * @param graph The graph in which to search for a path.
	 * @param src Name of the node in the graph where the path must start.
	 * @param dest Name of the node in the graph where the path must end.
	 * @spec.requires graph is not null, src and dest name nodes in graph
	 * @return Returns the least weighted (and lexicographically least) path from 
	 * 		the node with name matching src to the one with name matching dest.
	 * 		This path is returned in a List&lt;GraphEdge&lt;String, String&gt;&gt;, or null if no path
	 * 		exists between src and dest
	 */
	public static <K extends Comparable<K>> List<GraphEdge<K, Double>> shortestPath(Graph<K, Double> graph, 
			K src, K dest) {
		//passes in Comparator for comparing paths
		PriorityQueue<ArrayList<GraphEdge<K, Double>>> active = 
				new PriorityQueue<ArrayList<GraphEdge<K, Double>>>(5, new PathComparator<K>());
		Set<GraphNode<K, Double>> finished = new HashSet<GraphNode<K, Double>>();
		
		GraphNode<K, Double> startNode = graph.getNode(src);
		GraphNode<K, Double> destNode = graph.getNode(dest);
		GraphEdge<K, Double> startEdge = new GraphEdge<K, Double>(0.0, startNode);
		ArrayList<GraphEdge<K, Double>> startPath = new ArrayList<GraphEdge<K, Double>>();
		startPath.add(startEdge);
		active.add(startPath);
		
		// {{ Inv: have the least cost path to every finished node and active contains
		//		all paths of the form p + [c], where p is the shortest path to some 
		//		finished node n and c is a child of n }}
		while(!active.isEmpty()) {
			List<GraphEdge<K, Double>> minPath = active.remove();
			GraphNode<K, Double> minDest = minPath.get(minPath.size() - 1).getNode();
			
			if (minDest.compareTo(destNode) == 0) {
				//remove initial edge to start
				if (minPath.get(0).compareTo(startEdge) ==  0) {
					minPath.remove(0);
				}
				return minPath;
			}
			if (finished.contains(minDest)) {
				continue;
			}
			
			finished.add(minDest);
			// {{ Inv: for every child node of minDest that we have seen so far is in 
			//		finished, with all paths in the form of p + [c] in active }}
			for (GraphNode<K, Double> node : minDest.getChildren()) {
				if (!finished.contains(node)) {
					// {{ Inv: for every edge that leads from minDest to the current child,
					//		a new path has been made and added to active in the form of 
					//		p + [c] }}
					for (GraphEdge<K, Double> edge : minDest.getEdgesFromNode(node)) {
						ArrayList<GraphEdge<K, Double>> newPath = 
								new ArrayList<GraphEdge<K, Double>>(minPath);
						newPath.add(edge);
						active.add(newPath);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a graph object describing the contents of the given file. Edges between nodes
	 * have labels that represent their "weight," or the inverse of the number of connections
	 * between them
	 * 
	 * @param fileName The simple file name of the .TSV file to read from. 
	 * @throws MalformedDataException if the file cannot be parsed.
	 * @return A Graph&lt;String, Double&gt; object constructed from the contents of given file.
	 */
	public static Graph<String, Double> buildGraph(String fileName) 
			throws MalformedDataException {
		Graph<String, String> initGraph = buildGraphFromFile(fileName);
		Set<GraphNode<String, String>> chars = initGraph.getNodes();
		Set<GraphNode<String, Double>> weightedChars = 
				new HashSet<GraphNode<String, Double>>();
		List<GraphNode<String, String>> unweightedList = 
				new ArrayList<GraphNode<String, String>>();
		List<GraphNode<String, Double>> charList = 
				new ArrayList<GraphNode<String, Double>>();
		
		for (GraphNode<String, String> charNode : chars) {
			GraphNode<String, Double> newChar = 
					new GraphNode<String, Double>(charNode.getData());
			weightedChars.add(newChar);
			charList.add(newChar);
			unweightedList.add(charNode);
		}
		
		//constructs a weighted graph based on number of 
		//connections for each char from initGraph
		Graph<String, Double> weightedGraph = new Graph<String, Double>(weightedChars);
		// {{ Inv: for characters unweightedList.get(0),...,unweightedList.get(i - 1), all 
		//		connections between each character and all other characters have been converted
		//		into weightings, and is reflected in charList.get(0),...,charList.get(i - 1 }}
		for (int i = 0; i < unweightedList.size(); i++) {
			GraphNode<String, String> initStart = unweightedList.get(i);
			// {{ Inv: for characters unweightedList.get(i + 1),...,unweightedList.get(j - 1),
			//		all connections between each character and unweightedList.get(0),...,
			//		unweightedList.get(i) have been converted into weightings, and is 
			//		reflected in charList.get(0),..,charList.get(j - 1) }}
			for (int j = i + 1; j < unweightedList.size(); j++) {
				GraphNode<String, String> initEnd = unweightedList.get(j);
				Set<GraphEdge<String, String>> connections = 
						initStart.getEdgesFromNode(initEnd);
				if (connections != null) {
					double numConnections = connections.size();
					double cost = 1.0 / numConnections;
					GraphNode<String, Double> weightedStart = charList.get(i);
					GraphNode<String, Double> weightedEnd = charList.get(j);
					assert(initStart.getData().equals(weightedStart.getData()));
					assert(initEnd.getData().equals(weightedEnd.getData()));
					GraphEdge<String, Double> newEdgeToEnd = 
							new GraphEdge<String, Double>(cost, weightedEnd);
					weightedStart.addEdge(newEdgeToEnd);
					GraphEdge<String, Double> newEdgeToStart = 
							new GraphEdge<String, Double>(cost, weightedStart);
					weightedEnd.addEdge(newEdgeToStart);
				}
			}
		}
		return weightedGraph;
	}

	/**
	 * Returns a graph object describing the contents of the given file.
	 *
	 * @param fileName The simple file name of the .TSV file to read from.
	 * @throws MalformedDataException if the file cannot be parsed.
	 * @return A Graph&lt;String, String&gt; object constructed from the contents of given file.
	 */
	public static Graph<String, String> buildGraphFromFile(String fileName) throws MalformedDataException {
		Set<String> characters = new HashSet<String>();
		Map<String, List<String>> books = new HashMap<String, List<String>>();
		parseData(fileName, characters, books);

		Set<GraphNode<String, String>> charNodes = new HashSet<GraphNode<String, String>>();
		for (String name : characters) {
			GraphNode<String, String> node = new GraphNode<String, String>(name);
			charNodes.add(node);
		}
		Graph<String, String> graph = new Graph<String, String>(charNodes);
		// {{ Inv: for every book in books that we have seen so far, all connections between
		//		characters that appear in that book has been made }}
		for (String book : books.keySet()) {
			List<String> connections = books.get(book);
			// {{ Inv: for characters connections.get(0),...,connections.get(i - 1), all
			//		connections between them and all other characters that appear in this book
			//		has been made }}
			for (int i = 0; i < connections.size(); i++) {
				GraphNode<String, String> start = graph.getNode(connections.get(i));
				// {{ Inv: for characters connections.get(i + 1),...,connections.get(j - 1), all
				//		connections between them and connections.get(0),...,connections.get(i)
				//		has been made for this book }}
				for (int j = i + 1; j < connections.size(); j++) {
					GraphNode<String, String> end = graph.getNode(connections.get(j));
					GraphEdge<String, String> edgeOne = new GraphEdge<String, String>(book, end);
					start.addEdge(edgeOne);
					GraphEdge<String, String> edgeTwo = new GraphEdge<String, String>(book, start);
					end.addEdge(edgeTwo);
				}
			}
		}
		return graph;
	}

	public static class MalformedDataException extends Exception {
		public MalformedDataException() { }

		public MalformedDataException(String message) {
			super(message);
		}

		public MalformedDataException(Throwable cause) {
			super(cause);
		}

		public MalformedDataException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	/**
	 * Reads the dataset.
	 * Each line of the input file contains a character name and a comic
	 * book the character appeared in, separated by a tab character
	 *
	 * @spec.requires filename is a valid file path
	 * @param filename the file that will be read
	 * @param characters list in which all character names will be stored;
	 *          typically empty when the routine is called
	 * @param books map from titles of comic books to characters that
	 *          appear in them; typically empty when the routine is called
	 * @spec.modifies characters, books
	 * @spec.effects fills characters with a list of all unique character names
	 * @spec.effects fills books with a map from each comic book to all characters
	 *          appearing in it
	 * @throws MalformedDataException if the file is not well-formed:
	 *          each line contains exactly two tokens separated by a tab,
	 *          or else starting with a # symbol to indicate a comment line.
	 */
	public static void parseData(String filename, Set<String> characters,
								 Map<String, List<String>> books) throws MalformedDataException {
        // Why does this method accept the Collections to be filled as
        // parameters rather than making them a return value? To allows us to
        // "return" two different Collections. If only one or neither Collection
        // needs to be returned to the caller, feel free to rewrite this method
        // without the parameters. Generally this is better style.
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));

            // Construct the collections of characters and books, one
            // <character, book> pair at a time.
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {

                // Ignore comment lines.
                if (inputLine.startsWith("#")) {
                    continue;
                }

                // Parse the data, stripping out quotation marks and throwing
                // an exception for malformed lines.
                inputLine = inputLine.replace("\"", "");
                String[] tokens = inputLine.split("\t");
                if (tokens.length != 2) {
                    throw new MalformedDataException("Line should contain exactly one tab: "
                            + inputLine);
                }

                String character = tokens[0];
                String book = tokens[1];

                // Add the parsed data to the character and book collections.
                characters.add(character);
                if (!books.containsKey(book)) {
                    books.put(book, new ArrayList<String>());
                }
                books.get(book).add(character);
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println(e.toString());
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
