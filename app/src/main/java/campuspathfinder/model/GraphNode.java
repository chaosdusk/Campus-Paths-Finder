package campuspathfinder.model;

import java.util.*;

/**
 * A GraphNode is a mutable representation of a node on the Graph.
 * A node supports multiple direct, labeled edges. A node also holds data.
 * 
 * Identical edges are not supported, i.e., there cannot be two edges coming out of
 * the same node that points to the node with the same data and with the same label.
 * 
 * @param <K> The key/name/data of a GraphNode/the data that a GraphNode holds
 * @param <L> The label of a GraphEdge
 * 
 * Specification fields:
 * 		@specfield data : String // Data of the node
 * 		@specfield edges : Set&lt;GraphEdge&gt; // Edges of the node
 * 		@specfield children : Set&lt;GraphNode&gt; // Children of this
 * 
 * Abstract Invariant: a GraphNode must have data
 *
 */

public class GraphNode<K extends Comparable<K>, L extends Comparable<L>> 
	implements Comparable<GraphNode<K, L>> {
	
	/*Abstract Function:
	 * A GraphNode, n, represents a node on a graph. n has a number of edges 
	 * coming out of it, pointing to other nodes or n itself.
	 * 
	 *Representation Invariant:
	 * data != null && edges != null && children != null
	 * for (0 <= i < edges.size(), edges.get(i).getNode() != null) &&
	 * for (0 <= i < edges.size(), i < j < edges.size(), 
	 * !edges.get(i).getLabel().equals(edges.get(j).getLabel()) || 
	 * edges.get(i).getNode() != edges.get(j).getNode())
	 * 
	 * In other words, no edges coming out of n does not point to a node and
	 * no two edges that point to the same node have the same label 
	 * 
	 */
	
	//Holds the data of the node
	private K data;
	
	//Holds the edges that the node has
	private Set<GraphEdge<K, L>> edges;
	
	//Holds the children that the node has
	private Set<GraphNode<K, L>> children;
	
	/**
	 * @param data The String that the node is to hold
	 * @spec.effects Creates a new instance of GraphNode with given data
	 * @spec.requires data != null
	 * @throws IllegalArgumentException if data == null
	 */
	public GraphNode(K data) {
		if (data == null) {
			throw new IllegalArgumentException("data is null");
		}
		this.data = data;
		edges = new HashSet<GraphEdge<K, L>>();
		children = new HashSet<GraphNode<K, L>>();
		checkRep();
	}
	
	/**
	 * 
	 * @param data The String that the node is to hold
	 * @param edges An array of GraphEdges that connects out of the node
	 * @spec.effects Creates a new instance of GraphNode with given data
	 * 				 and a number of edges connecting out of the node
	 * @spec.requires data != null
	 * @throws IllegalArgumentException if data == null
	 */
	public GraphNode(K data, Set<GraphEdge<K, L>> edges) {
		if (data == null) {
			throw new IllegalArgumentException("data is null");
		}
		this.data = data;
		this.edges = new HashSet<GraphEdge<K, L>>();
		for (GraphEdge<K, L> edge : edges) {
			if (!isEdge(edge)) {
				this.edges.add(edge);
			}
		}
		children = new HashSet<GraphNode<K, L>>();
		// {{ Inv: for each edge in this.edges that we have seen so far, the node
		//	  it points to has been added to children }}
		for (GraphEdge<K, L> edge : this.edges) {
			children.add(edge.getNode());
		}
		checkRep();
	}
	
	/**
	 * Returns the data of the node
	 * 
	 * @return A String with data that the node holds
	 */
	public K getData() {
		checkRep();
		return this.data;
	}
	
	/**
	 * Returns a fresh, read-only access sorted set of outgoing edges
	 * Edges are sorted lexicographically by label
	 * 
	 * @return Set&lt;GraphEdge&gt; of edges connecting out of the node
	 */
	public Set<GraphEdge<K, L>> getEdges() {
		checkRep();
		Set<GraphEdge<K, L>> set = new TreeSet<GraphEdge<K, L>>(edges);
		return Collections.unmodifiableSet(set);
	}
	
	/**
	 * Sets the data of the node
	 * 
	 * @param data The new data that the node holds
	 * @spec.requires data != null
	 * @throws IllegalArgumentException if data == null
	 * @spec.modifies the data that this node holds
	 */
	public void setData(K data) {
		checkRep();
		if (data == null) {
			throw new IllegalArgumentException("data is null");
		}
		this.data = data;
	}
	
	/**
	 * Adds edge from the node
	 * 
	 * @param edge The edge to be added
	 * @spec.requires !isEdge(edge)
	 * @throws IllegalArgumentException if isEdge(edge)
	 * @spec.modifies adds an outgoing edge
	 */
	public void addEdge(GraphEdge<K, L> edge) {
		checkRep();
		if (isEdge(edge)) {
			throw new IllegalArgumentException("edge is already in the node");
		}
		edges.add(edge);
		if (!children.contains(edge.getNode())) {
			children.add(edge.getNode());
		}
	}
	
	/**
	 * Removes edge from the node
	 * 
	 * @param edge The edge to be removed
	 * @spec.requires isEdge(edge)
	 * @throws IllegalArgumentException if !isEdge(edge)
	 * @spec.modifies removes an outgoing edge from the node
	 */
	public void removeEdge(GraphEdge<K, L> edge) {
		checkRep();
		if (!isEdge(edge)) {
			throw new IllegalArgumentException("edge is not a valid edge");
		}
		edges.remove(edge);

		//updates children
		GraphNode<K, L> child = edge.getNode();
		boolean contains = false;
		for (GraphEdge<K, L> e : edges) {
			if(e.getNode().compareTo(child) == 0) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			children.remove(child);
		}
	}
	
	/**
	 * Checks if given edge is a valid edge connecting out of the node
	 * 
	 * @param edge The GraphEdge to be checked
	 * @return true if and only if edge is a valid edge
	 */
	public boolean isEdge(GraphEdge<K, L> edge) {
		return edges.contains(edge);
	}
	
	/**
	 * Returns the edges that traverses to the given node
	 * 
	 * @param node The destination node
	 * @return Set&lt;GraphEdge&gt; of sorted GraphEdges that points to the node, 
	 * 		   or null if no GraphEdge points to the node. GraphEdges are sorted
	 * 		   lexicographically by label
	 */
	public Set<GraphEdge<K, L>> getEdgesFromNode(GraphNode<K, L> node) {
		checkRep();
		if (!children.contains(node)) {
			return null;
		}
		Set<GraphEdge<K, L>> set = new TreeSet<GraphEdge<K, L>>();
		// {{ Inv: for each edge in edges that we have seen so far, if the edge 
		//	  points to node, then it has been added to set }}
		for (GraphEdge<K, L> edge : edges) {
			if(edge.getNode() == node) {
				set.add(edge);
			}
		}
		return set;
	}
	
	/**
	 * Returns an edge that traverses to the given node
	 * 
	 * @param node The destination node
	 * @return GraphEdge that points to the node, or null if no GraphEdge points 
	 * 		to the node. 
	 */
	public GraphEdge<K, L> getEdgeFromNode(GraphNode<K, L> node) {
		checkRep();
		if (!children.contains(node)) {
			return null;
		}
		for (GraphEdge<K, L> edge : edges) {
			if (edge.getNode() == node) {
				return edge;
			}
		}
		return null;
	}
	
	/**
	 * Returns a fresh, read-only access set of child nodes, 
	 * sorted alphabetically by the data each node holds
	 * Changes to the children nodes will lead to unspecified behavior
	 * 
	 * @return Set&lt;GraphNode&gt; of nodes in the graph sorted alphabetically
	 */
	public Set<GraphNode<K, L>> getChildren() {
		checkRep();
		Set<GraphNode<K, L>> sortedChildren = new TreeSet<GraphNode<K, L>>(this.children);
		return Collections.unmodifiableSet(sortedChildren);
	}
	
	/**
	 * Returns a String representation of the node and all nodes that it points to,
	 * in (node,edge,node) format. See the Graph class specification for more.
	 * The nodes will appear in alphabetical order by node name and 
	 * secondarily by edge label
	 * 
	 * @return The String representation of the node
	 */
	public String toString() {
		checkRep();
		String retVal = "";
		if (this.children.size() > 0) {
		Set<GraphNode<K, L>> sortedChildren = getChildren();
			// {{ Inv: for each node in sortedChildren that we have seen so far, 
			//	  it has been added to retVal in the proper format (A, B, C),
			//	  where A is this and C is node}}
			for (GraphNode<K, L> node : sortedChildren) {
				Set<GraphEdge<K, L>> edgesToChild = getEdgesFromNode(node);
				// {{ Inv: for each edge in edgesToChild that we have seen so far,
				//	  it has been added to retVal in the proper format (A, B, C), 
				//	  where A is this, B is the edge, and C is node
				for (GraphEdge<K, L> edge : edgesToChild) {
					retVal += "(" + this.data + ", " + 
							edge.getLabel() + ", " + node.getData() + "), ";
				}
			}
			retVal = retVal.substring(0, retVal.length() - 2);
		} else {
			retVal = "(" + this.data + ", , " + this.data + ")";
		}
		return retVal;
	}
	
	/**
	 * Checks representation of this
	 * @throws IllegalStateException if rep invariant is not satisfied
	 */
	private void checkRep() {
		if (data == null || edges == null || children == null) {
			throw new IllegalStateException("Data is null or "
					+ "edges is null or children is null");
		}
	}

	/**
	 * Compares this.getData() to other.getData()
	 * 
	 * @param other GraphNode to be compared against
	 * @return the value 0 if the argument data is equal to this data; 
	 * 		   a value less than 0 if this data is lexicographically 
	 * 		   less than the argument data; 
	 * 		   and a value greater than 0 if this data is lexicographically 
	 *		   greater than the argument data.
	 */
	public int compareTo(GraphNode<K, L> other) {
		checkRep();
		return this.data.compareTo(other.getData());
	}
}
