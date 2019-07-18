package campuspathfinder.model;

import java.util.*;

/**
 * Graph represents a mutable mathematical graph. The graph is directed,
 * supports multiple edges between the same pair of nodes, and has labeled edges.
 * 
 * An example of a pair of nodes is (A, B, C), where B is the edge that takes node A
 * to node C. Another example is (A, , A), where node A has no edges connecting to 
 * or out of. Nodes will be represented with their data, and edges with the label.
 * 
 * Identical edges are not supported, i.e., there cannot be two edges coming out of
 * the same node that points to the same node with the same label.
 * 
 * Identical nodes are not supported, i.e., there cannot be two nodes with
 * the same data
 * 
 * @param <K> The key/name/data of a GraphNode/the data that a GraphNode holds
 * @param <L> The label of a GraphEdge
 * 
 * Specification fields: 
 * 		@specfield nodes : Map&lt;String, GraphNode&gt; 
 * 									// All nodes present in the graph
 * 
 */

public class Graph<K extends Comparable<K>, L extends Comparable<L>> {
	
	/*Abstraction Function:
	 * A Graph, g, represents a mathematical graph composed of GraphNodes
	 * and GraphEdges, where GraphNodes are contained in 'nodes.'
	 * If there are no GraphNodes in 'nodes,' then the Graph is empty.
	 * 
	 * Representation Invariant:
	 * nodes != null &&
	 * for (0 <= i < nodes.size(), nodes.get(i) != null)
	 * 
	 * In other words, no nodes in the graph are null
	 * 
	 */
	
	//Holds all nodes added
	private Map<K, GraphNode<K, L>> nodes;
	
	/**
	 * @spec.effects Constructs a new Graph object with no nodes or edges
	 */
	public Graph() {
		nodes = new HashMap<K, GraphNode<K, L>>();
		checkRep();
	}
	
	/**
	 * @param nodes The nodes that this Graph will hold
	 * @spec.requires nodes != null
	 * @throws IllegalArgumentException if nodes == null
	 * @spec.effects Constructs a new Graph object with nodes as its nodes
	 */
	public Graph(Set<GraphNode<K, L>> nodes) {
		if (nodes == null) {
			throw new IllegalArgumentException("nodes is null");
		}
		this.nodes = new HashMap<K, GraphNode<K, L>>();
		for (GraphNode<K, L> node : nodes) {
			if (node != null) {
				this.nodes.put(node.getData(), node);
			}
		}
		checkRep();
	}
	
	/**
	 * Adds an edge between two nodes with given label. 
	 * If an edge already exists, the method does nothing. 
	 * If end is not a node in the graph, add it as a new node.
	 * 
	 * @param start The start node with edge coming out of it
	 * @param end The end node with edge pointing to it
	 * @param label The label of the edge that is supposed to point to end
	 * @spec.requires start is an existing node in the graph
	 * @spec.requires end != null
	 * @throws IllegalArgumentException if start is not an existing node
	 * @spec.modifies adds an outgoing edge to start
	 */
	public void addEdge(GraphNode<K, L> start, GraphNode<K, L> end, L label) {
		checkRep();
		if (!nodes.containsKey(start.getData())) {
			throw new IllegalArgumentException("start is not an existing node");
		}
		if(end != null) {
			if(!nodes.containsKey(end.getData())) {
				nodes.put(end.getData(), end);
			}
			GraphEdge<K, L> edge = new GraphEdge<K, L>(label, end);
			start.addEdge(edge);
		}
	}
	
	/**
	 * Adds a new node to the graph
	 * 
	 * @param node The node to be added
	 * @spec.requires node != null
	 * @spec.effects adds the node to the Graph
	 */
	public void addNode(GraphNode<K, L> node) {
		checkRep();
		if (node != null) {
			nodes.put(node.getData(), node);
		}
	}
	
	/**
	 * Inserts node in between start and end, with the new edges having the same label
	 * as the old edge connecting start to end. 
	 * If there is more than one edge going from start to end, the label of the new
	 * edges will be from one of the existing edges.
	 * If an edge does not already exist, 
	 * then the label will be a single space.
	 * The old edge between start and end will be removed
	 * 
	 * @param start The start node for that connects to node
	 * @param end The end node that node connects to
	 * @param node The node to be in between start and end
	 * @spec.requires start and end are existing nodes in the graph and node != null
	 * @throws IllegalArgumentException if start or end are not existing nodes in the
	 * 		   graph or node == null
	 * @spec.effects inserts the new node into the specified location
	 */
	@SuppressWarnings("unchecked")
	public void insert(GraphNode<K, L> start, GraphNode<K, L> end, GraphNode<K, L> node) {
		checkRep();
		if(!nodes.containsKey(start.getData()) 
				|| !nodes.containsKey(end.getData()) || node == null) {
			throw new IllegalArgumentException("Start or end are not existing "
					+ "nodes or node is null");
		}
		Set<GraphNode<K, L>> children = start.getChildren();
		L label;
		if (children.contains(end)) {
			GraphEdge<K, L> edge = start.getEdgesFromNode(end).iterator().next();
			label = edge.getLabel();
			start.removeEdge(edge);
		} else {
			label = (L) " ";
		}
		GraphEdge<K, L> edge1 = new GraphEdge<K, L>(label, node);
		start.addEdge(edge1);
		GraphEdge<K, L> edge2 = new GraphEdge<K, L>(label, end);
		node.addEdge(edge2);
	}
	
	/**
	 * Removes node from the graph, all edges and unconnected nodes
	 * that comes after the node will be lost
	 * 
	 * @param node The node to be removed
	 * @spec.requires node is an existing node in the graph
	 * @spec.effects removes the node from the Graph
	 */
	public void removeNode(GraphNode<K, L> node) {
		checkRep();
		nodes.remove(node.getData());
	}
	
	/**
	 * Traverses the given path labels from a given node
	 * 
	 * @param start The node to start at
	 * @param edges The path to traverse, in order of steps
	 * @return The GraphNode at which the edges from start points to, or null if it
	 * 		   is not a valid path or edges == null
	 * @spec.requires start is an existing node 
	 */
	public GraphNode<K, L> traverse(GraphNode<K, L> start, List<GraphEdge<K, L>> edges) {
		checkRep();
		GraphNode<K, L> retVal = null;
		if (nodes.containsKey(start.getData())) {
			if (edges == null) {
				return null;
			}
			retVal = start;
			// {{ Inv: for each edge in edges we have seen so far, 
			//	  edge is a valid edge to traverse to }}
			for (GraphEdge<K, L> edge : edges) {
				if (retVal.isEdge(edge)) {
					retVal = edge.getNode();
				} else {
					return null;
				}
			}
		}
		return retVal;
	}
	
	/**
	 * Returns a String representation of the current Graph
	 * The String representation will be in the form of (node,edge,node) groups, see
	 * the Graph class definition for a more in-depth explanation of the 
	 * representation.
	 * The nodes will appear in alphabetical order by node name and 
	 * secondarily by edge label
	 * 
	 * @return A String of the current map
	 */
	public String toString() {
		checkRep();
		String retVal = "";
		Set<GraphNode<K, L>> sortedNode = getSortedNodes();
		// {{ Inv: for each node in nodes that we have seen so far, node.toString()
		//	  has been added to retVal in the proper format (A, B, C), where
		//	  A is node, B is an edge that points to C, and C is a child node of A }}
		for (GraphNode<K, L> node : sortedNode) {
			retVal += node.toString() + ", ";
		}
		if (retVal.length() > 2) {
			retVal = retVal.substring(0, retVal.length() - 2);
		}
		return retVal;
	}
	
	/**
	 * Checks if a node is in the current Graph
	 * 
	 * @param node The node to be checked against
	 * @return true if and only if node is in the Graph
	 */
	public boolean isNode(GraphNode<K, L> node) {
		checkRep();
		return nodes.containsKey(node.getData());
	}
	
	/**
	 * Returns number of nodes in the graph
	 * 
	 * @return int that represents the number of nodes in the graph
	 */
	public int size() {
		checkRep();
		return nodes.size();
	}
	
	/**
	 * Returns a read-only set of nodes in the graph
	 * 
	 * @return Set&lt;GraphNode&gt; of nodes in the graph
	 */
	public Set<GraphNode<K, L>> getNodes() {
		checkRep();
		Set<GraphNode<K, L>> set = new HashSet<GraphNode<K, L>>();
		for (K data : nodes.keySet()) {
			set.add(nodes.get(data));
		}
		return Collections.unmodifiableSet(set);
	}
	
	/**
	 * Returns the node with given data, if it exists
	 * 
	 * @param data The data to be searched for
	 * @return a GraphNode whose stored data matches with argument, or null if no
	 * 		   nodes match
	 */
	public GraphNode<K, L> getNode(K data) {
		return nodes.get(data);
	}
	
	/**
	 * Returns a read-only set of nodes in the graph, 
	 * sorted alphabetically by the data each node holds
	 * 
	 * @return Set&lt;GraphNode&gt; of nodes in the graph 
	 * 		sorted alphabetically
	 */
	public Set<GraphNode<K, L>> getSortedNodes() {
		checkRep();
		Set<GraphNode<K, L>> set = new TreeSet<GraphNode<K, L>>(getNodes());
		return set;
	}
	
	/**
	 * Returns an iterator of the graph, iterating over the nodes in the graph
	 * in no guaranteed order
	 * 
	 * @return Iterator&lt;GraphNode&gt; of the graph, iterating over the 
	 * 			nodes in the graph
	 */
	public Iterator<GraphNode<K, L>> iterator() {
		checkRep();
		return getNodes().iterator();
	}
	
	/**
	 * Checks representation of this
	 * @throws IllegalStateException if rep invariant is not satisfied
	 */
	private void checkRep() {
		if (nodes == null) {
			throw new IllegalStateException("Graph is empty");
		}
		if (nodes.get(null) instanceof GraphNode) {
			throw new IllegalStateException("Graph contains null node");
		}
	}
}
