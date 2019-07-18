package campuspathfinder.model.GraphADT;

import static org.junit.Assert.*;
import org.junit.Test;
import campuspathfinder.model.*;

/**
 * This class contains a set of test cases that can be used to test the implementation
 * of the GraphEdge class.
 *
 */
public class GraphEdgeTest {

	private GraphEdge<String, String> one;

	private GraphEdge<String, String> two;
	
	private GraphNode<String, String> three = new GraphNode<String, String>("data");
	
	//Constructor Tests
	
	/**
	 * Tests constructor
	 */
	@Test
	public void testConstructor() {
		three = new GraphNode<String, String>("data");
		one = new GraphEdge<String, String>("label", three);
		try {
			new GraphEdge<String, String>("label", null);
			throw new IllegalStateException();
		} catch (IllegalArgumentException e){}
		try {
			new GraphEdge<String, String>(null, three);
			throw new IllegalStateException();
		} catch (IllegalArgumentException e){}
	}
	
	// Get Label test
	
	/**
	 * Tests getLabel()
	 */
	@Test
	public void testGetLabel() {
		one = new GraphEdge<String, String>("label", three);
		assertEquals("label",one.getLabel());
	}
	
	//Get Node test
	
	/**
	 * Tests getNode()
	 */
	@Test
	public void testGetNode() {
		one = new GraphEdge<String, String>("label", three);
		assertEquals(three, one.getNode());
	}
	
	//Compare To test
	
	/**
	 * Tests compareTo()
	 */
	@Test
	public void testCompareTo() {
		one = new GraphEdge<String, String>("beta", new GraphNode<String, String>("data"));
		two = new GraphEdge<String, String>("alpha", new GraphNode<String, String>("data"));
		assertEquals(-1, two.compareTo(one));
	}
}
