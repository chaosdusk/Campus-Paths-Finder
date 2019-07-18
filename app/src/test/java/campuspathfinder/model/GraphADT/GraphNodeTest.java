package campuspathfinder.model.GraphADT;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import campuspathfinder.model.*;

/**
 * This class contains a set of test cases that can be used to test the implementation
 * of the GraphNode class.
 *
 */
public class GraphNodeTest {
	
	private GraphNode<String, String> one;

	private GraphNode<String, String> two;

	private GraphEdge<String, String> edgeOne;

	private GraphEdge<String, String> edgeTwo;
	
	private Set<GraphEdge<String, String>> set;

	//Constructor test
	
	/**
	 * Tests constructor
	 */
	@Test
	public void testOneArgConstructor() {
		one = new GraphNode<String, String>("data");
		two = new GraphNode<String, String>("");
		try {
			new GraphNode<String, String>(null);
			throw new IllegalStateException();
		} catch (IllegalArgumentException e) {}
	}
	
	/**
	 * Tests constructor
	 */
	@Test
	public void testTwoArgConstructor() {
		set = new HashSet<GraphEdge<String, String>>();
		set.add(new GraphEdge<String, String>("l", new GraphNode<String, String>("data")));
		set.add(new GraphEdge<String, String>("l", new GraphNode<String, String>("data")));
		set.add(new GraphEdge<String, String>("l", new GraphNode<String, String>("data")));
		one = new GraphNode<String, String>("data", set);
		two = new GraphNode<String, String>("data2", new TreeSet<GraphEdge<String, String>>());
	}
	
	//Get Data test
	
	/**
	 * Tests getData()
	 */
	@Test
	public void testGetData() {
		one = new GraphNode<String, String>("data");
		two = new GraphNode<String, String>("data2");
		assertEquals("data", one.getData());
		assertEquals("data2", two.getData());
	}
	
	//Is Edge test
	
	/**
	 * Test isEdge()
	 */
	@Test
	public void testIsEdge() {
		edgeOne = new GraphEdge<String, String>("label", new GraphNode<String, String>("data"));
		set = new HashSet<GraphEdge<String, String>>();
		set.add(edgeOne);
		one = new GraphNode<String, String>("data", set);
		assertTrue(one.isEdge(edgeOne));
	}
	
	//Get Edges test
	
	/**
	 * Tests getEdges()
	 */
	@Test
	public void testGetEdges() {
		set = new TreeSet<GraphEdge<String, String>>();
		set.add(new GraphEdge<String, String>("la", new GraphNode<String, String>("data")));
		set.add(new GraphEdge<String, String>("lb", new GraphNode<String, String>("data2")));
		set.add(new GraphEdge<String, String>("lc", new GraphNode<String, String>("data3")));
		one = new GraphNode<String, String>("data", set);
		Set<GraphEdge<String, String>> newSet = one.getEdges();
		Iterator<GraphEdge<String, String>> setItr = set.iterator();
		Iterator<GraphEdge<String, String>> newSetItr = newSet.iterator();
		assertEquals(setItr.next().getLabel(), newSetItr.next().getLabel());
		assertEquals(setItr.next().getLabel(), newSetItr.next().getLabel());
		assertEquals(setItr.next().getLabel(), newSetItr.next().getLabel());
	}
	
	//Get Edges deep copy test
	
	/**
	 * Tests getEdges() deep copying
	 */
	@Test
	public void testGetEdgesCopy() {
		set = new HashSet<GraphEdge<String, String>>();
		set.add(new GraphEdge<String, String>("a", new GraphNode<String, String>("data")));
		set.add(new GraphEdge<String, String>("b", new GraphNode<String, String>("data")));
		set.add(new GraphEdge<String, String>("c", new GraphNode<String, String>("data")));
		one = new GraphNode<String, String>("data", set);
		Set<GraphEdge<String, String>> newList = one.getEdges();
		GraphEdge<String, String> edge = new GraphEdge<String, String>("label", new GraphNode<String, String>("data"));
		one.addEdge(edge);
		assertFalse(newList.contains(edge));
	}
	
	//Set Data test
	
	/**
	 * Tests setData()
	 */
	@Test
	public void testSetData() {
		one = new GraphNode<String, String>("data");
		one.setData("new data");
		assertEquals("new data", one.getData());
		
		try {
			one.setData(null);
			throw new IllegalStateException();
		} catch (IllegalArgumentException e) {}
	}
	
	//Add edge test
	
	/**
	 * Tests addEdge()
	 */
	@Test
	public void testAddEdge() {
		one = new GraphNode<String, String>("data");
		edgeOne = new GraphEdge<String, String>("label", one);
		one.addEdge(edgeOne);
		assertEquals(edgeOne.getLabel(), 
				one.getEdges().iterator().next().getLabel());
		assertEquals(edgeOne.getNode().getData(), 
				one.getEdges().iterator().next().getNode().getData());
	}
	
	//Remove edge test
	
	/**
	 * Tests removeEdge()
	 */
	@Test
	public void testRemoveEdge() {
		edgeOne = new GraphEdge<String, String>("label", new GraphNode<String, String>("data"));
		set = new HashSet<GraphEdge<String, String>>();
		set.add(edgeOne);
		one = new GraphNode<String, String>("data", set);
		one.removeEdge(edgeOne);
		assertFalse(one.isEdge(edgeOne));
	}
	
	/**
	 * Tests removeEdge() removing a child case
	 */
	@Test
	public void testRemoveEdgeRemoveChild() {
		GraphNode<String, String> child = new GraphNode<String, String>("data");
		edgeOne = new GraphEdge<String, String>("label", child);
		set = new HashSet<GraphEdge<String, String>>();
		set.add(edgeOne);
		one = new GraphNode<String, String>("data", set);
		one.removeEdge(edgeOne);
		assertFalse(one.getChildren().contains(child));
	}
	
	//Get Edge From Node test
	
	/**
	 * Tests getEdgesFromNode()
	 */
	@Test
	public void testGetEdgesFromNode() {
		one = new GraphNode<String, String>("data");
		edgeOne = new GraphEdge<String, String>("label", one);
		two = new GraphNode<String, String>("data2");
		two.addEdge(new GraphEdge<String, String>("label", new GraphNode<String, String>("data")));
		two.addEdge(edgeOne);
		assertEquals(edgeOne, two.getEdgesFromNode(one).iterator().next());
		edgeTwo = new GraphEdge<String, String>("abel", one);
		two.addEdge(edgeTwo);
		assertEquals(edgeTwo, two.getEdgesFromNode(one).iterator().next());
	}
	
	//Get Edge From Node test
	
	/**
	 * Tests getEdgeFromNode()
	 */
	@Test
	public void testGetEdgeFromNode() {
		one = new GraphNode<String, String>("data");
		edgeOne = new GraphEdge<String, String>("label", one);
		two = new GraphNode<String, String>("data2");
		two.addEdge(new GraphEdge<String, String>("label", new GraphNode<String, String>("data")));
		two.addEdge(edgeOne);
		assertEquals(edgeOne, two.getEdgeFromNode(one));
	}
	
	//Get Children test
	 
	/**
	 * Tests getChildren()
	 */
	@Test
	public void testGetChildren() {
		one = new GraphNode<String, String>("data");
		two = new GraphNode<String, String>("beta");
		GraphNode<String, String> three = new GraphNode<String, String>("alpha");
		GraphNode<String, String> node = new GraphNode<String, String>("node");
		GraphEdge<String, String> edge1 = new GraphEdge<String, String>("data", one);
		GraphEdge<String, String> edge2 = new GraphEdge<String, String>("beta", two);
		GraphEdge<String, String> edge3 = new GraphEdge<String, String>("alpha", three);
		set = new TreeSet<GraphEdge<String, String>>();
		set.add(edge3);
		set.add(edge2);
		set.add(edge1);
		node.addEdge(edge1);
		node.addEdge(edge2);
		node.addEdge(edge3);
		Set<GraphNode<String, String>> set2 = node.getChildren();
		Iterator<GraphEdge<String, String>> setItr = set.iterator();
		Iterator<GraphNode<String, String>> set2Itr = set2.iterator();
		assertEquals(setItr.next().getNode().getData(), 
					set2Itr.next().getData());
		assertEquals(setItr.next().getNode().getData(), 
				set2Itr.next().getData());
		assertEquals(setItr.next().getNode().getData(), 
				set2Itr.next().getData());
	}
	
	//To String test
	
	/**
	 * Tests toString()
	 */
	@Test
	public void testToString() {
		one = new GraphNode<String, String>("data");
		edgeOne = new GraphEdge<String, String>("label", one);
		one.addEdge(edgeOne);
		assertEquals("(data, label, data)", one.toString());
		two = new GraphNode<String, String>("data2");
		assertEquals("(data2, , data2)", two.toString());
		GraphNode<String, String> three = new GraphNode<String, String>("data3");
		edgeTwo = new GraphEdge<String, String>("label2", three);
		one.addEdge(edgeTwo);
		assertEquals("(data, label, data), (data, label2, data3)", one.toString());
	}
	
	//Compare To test
	
	/**
	 * Tests compareTo()
	 */
	@Test
	public void testCompareTo() {
		one = new GraphNode<String, String>("data");
		two = new GraphNode<String, String>("beta");
		assertEquals(-2, two.compareTo(one));
	}
}
