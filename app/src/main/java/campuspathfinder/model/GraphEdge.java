package campuspathfinder.model;

/**
 * A GraphEdge represents an immutable edge connecting one GraphNode to another.
 * An edge has a label and a node it points to.
 * 
 * @param <K> The key/name/data of a GraphNode/the data that a GraphNode holds
 * @param <L> The label of a GraphEdge
 * 
 * Specification fields:
 * 		@specfield label : String // The label of the edge
 * 		@specfield node : GraphNode // The node that the edge points to
 * 
 * Abstract Invariant: A GraphEdge must have a label and a node that it points to
 *
 */
public class GraphEdge<K extends Comparable<K>, L extends Comparable<L>> 
	implements Comparable<GraphEdge<K, L>> {
	
	/*Abstraction function:
	 * A GraphEdge, e, represents an edge from one node to another. e also has
	 * a label
	 * 
	 *Representation Invariant:
	 * label != null && !label.isEmpty() && node != null
	 * 
	 */
	
	//The label of the edge
	private final L label;
	
	//The node that the edge points to
	private final GraphNode<K, L> node;
	
	/**
	 * @param label The label of the edge
	 * @param node The node that the edge points to
	 * @spec.requires node != null and label != null
	 * @throws IllegalArgumentException if node == null or label == null
	 * @spec.effects Creates a new instance of GraphEdge with given label and node
	 */
	public GraphEdge(L label, GraphNode<K, L> node) {
		if (label == null || node == null) {
			throw new IllegalArgumentException("label is null"
					+ " or node is null");
		}
		this.label = label;
		this.node = node;
		checkRep();
	}
	
	/**
	 * Returns the label of the edge
	 * 
	 * @return The label of the edge
	 */
	public L getLabel() {
		checkRep();
		return this.label;
	}
	
	/**
	 * Returns the node that the edge points to
	 * 
	 * @return A GraphNode that the edge points to
	 */
	public GraphNode<K, L> getNode() {
		checkRep();
		return this.node;
	}
	
	/**
	 * Checks representation of this
	 */
	private void checkRep() {
		if (label == null || node == null) {
			throw new IllegalStateException("rep inv is not satisfied");
		}
	}
	/**
	 * Compares this.getLabel() against other.getLabel()
	 * 
	 * @param other The GraphEdge to be compared against
	 * @return the value 0 if the argument label is equal to this label; 
	 * 		   a value less than 0 if this label is lexicographically 
	 * 		   less than the argument label; 
	 * 		   and a value greater than 0 if this label is lexicographically 
	 *		   greater than the argument label.
	 */
	public int compareTo(GraphEdge<K, L> other) {
		if (this.label.compareTo((L) other.getLabel()) != 0) {
			return this.label.compareTo((L) other.getLabel());
		}
		return ((K) this.node.getData()).compareTo((K) other.getNode().getData());
	}
}
