package campuspathfinder.model.GraphADT;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import campuspathfinder.model.*;

/**
 * This class contains a set of test cases that can be used to test the implementation
 * of the GraphNode<String, String> class.
 *
 */
public class GraphTest {

	private Graph<String, String> one;
	
	Set<GraphNode<String, String>> set;
	
	//Constructor test
	
	/**
	 * Tests constructor
	 */
	@Test
	public void testConstructor() {
		new Graph<String, String>();
		set = new HashSet<GraphNode<String, String>>();
		new Graph<String, String>(set);
		set.add(new GraphNode<String, String>("data"));
		new Graph<String, String>(set);
	}
	
	//Add Edge test
	
	/**
	 * Tests adding an existing node
	 */
	@Test
	public void testAddEdge() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node2);
		one = new Graph<String, String>(set);
		one.addEdge(node1, node2, "label");
		assertEquals("label", node1.getEdges().iterator().next().getLabel());
	}
	
	/**
	 * Tests adding a new node
	 */
	@Test
	public void testAddEdgeNewNode() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		one = new Graph<String, String>(set);
		one.addEdge(node1, node2, "label");
		assertEquals("label", node1.getEdges().iterator().next().getLabel());
		assertEquals(node2.getData(), 
				node1.getEdges().iterator().next().getNode().getData());
	}
	
	//Insert test
	
	/**
	 * Tests inserting a node between two connecting nodes
	 */
	@Test
	public void testInsertConnecting() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");
		GraphNode<String, String> node3 = new GraphNode<String, String>("data3");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node2);
		one = new Graph<String, String>(set);
		GraphEdge<String, String> edge = new GraphEdge<String, String>("label", node2);
		node1.addEdge(edge);
		one.insert(node1, node2, node3);
		assertEquals("label", 
				node1.getEdgesFromNode(node3).iterator().next().getLabel());
		assertEquals("label", 
				node3.getEdgesFromNode(node2).iterator().next().getLabel());
	}
	
	/**
	 * Tests inserting a node between two not connecting nodes
	 */
	@Test
	public void testInsertNotConnecting() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");
		GraphNode<String, String> node3 = new GraphNode<String, String>("data3");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node2);
		one = new Graph<String, String>(set);
		one.insert(node1, node2, node3);
		assertEquals(" ", node1.getEdgesFromNode(node3).iterator().next().getLabel());
		assertEquals(" ", node3.getEdgesFromNode(node2).iterator().next().getLabel());
	}
	
	//Is Node test
	
	/**
	 * Tests isNode()
	 */
	@Test
	public void testIsNode() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node2);
		one = new Graph<String, String>(set);
		assertTrue(one.isNode(node2));
	}
	
	//Add Node test
	
	/**
	 * Tests add node
	 */
	@Test
	public void testAddNode() {
		one = new Graph<String, String>();
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		one.addNode(node1);
		assertTrue(one.isNode(node1));
	}
	
	//Remove Node test
	
	@Test
	public void testRemoveNode() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");	
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node2);
		one = new Graph<String, String>(set);
		one.removeNode(node2);
		assertFalse(one.isNode(node2));
	}
	
	//Traverse test
	
	/**
	 * Tests traversal
	 */
	@Test
	public void testTraverse() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");
		GraphNode<String, String> node3 = new GraphNode<String, String>("data3");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node2);
		set.add(node3);
		GraphEdge<String, String> edge1 = new GraphEdge<String, String>("label1", node2);
		GraphEdge<String, String> edge2 = new GraphEdge<String, String>("label2", node3);
		node1.addEdge(edge1);
		node2.addEdge(edge2);
		one = new Graph<String, String>(set);
		List<GraphEdge<String, String>> path = new ArrayList<GraphEdge<String, String>>();
		path.add(edge1);
		path.add(edge2);
		assertEquals("data3", one.traverse(node1, path).getData());
	}
	
	//To String test
	
	/**
	 * Tests toString()
	 */
	@Test
	public void testToString() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");
		GraphNode<String, String> node3 = new GraphNode<String, String>("data3");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node2);
		set.add(node3);
		GraphEdge<String, String> edge1 = new GraphEdge<String, String>("label1", node2);
		GraphEdge<String, String> edge2 = new GraphEdge<String, String>("label2", node3);
		GraphEdge<String, String> edge3 = new GraphEdge<String, String>("label3", node3);
		node1.addEdge(edge1);
		node1.addEdge(edge3);
		node2.addEdge(edge2);
		one = new Graph<String, String>(set);
		assertEquals("(data, label1, data2), (data, label3, data3),"
				+ " (data2, label2, data3), (data3, , data3)", one.toString());
	}
	
	//Size test
	
	/**
	 * Tests size()
	 */
	@Test
	public void testSize() {
		set = new HashSet<GraphNode<String, String>>();
		for (int i = 0; i < 10; i++) {
			set.add(new GraphNode<String, String>("data" + i));
		}
		one = new Graph<String, String>(set);
		assertEquals(10, one.size());
	}
	
	//Get Nodes test
	
	/**
	 * Tests getNodes()
	 */
	@Test
	public void testGetNodes() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("data2");
		GraphNode<String, String> node3 = new GraphNode<String, String>("data3");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node2);
		set.add(node3);
		one = new Graph<String, String>(set);
		Set<GraphNode<String, String>> set2 = new HashSet<GraphNode<String, String>>();
		set2.add(node1);
		set2.add(node2);
		set2.add(node3);
		set = one.getNodes();
		Iterator<GraphNode<String, String>> setItr = set.iterator();
		// {{ Inv: set2.get(0).getData().equals(set.get(0).getData()),...,
		//	  set2.get(i).getData().equals(set.get(i).getData()) }}
		for (int i = 0; i < set.size(); i++) {
			GraphNode<String, String> node = setItr.next();
			Boolean equal = false;
			Iterator<GraphNode<String, String>> set2Itr = set2.iterator();
			int j = 0;
			// {{ Inv2: Inv && !set.get(i).getData().equals(set2.get(0).getData()),..,
			//	  !set.get(i).getData().equals(set2.get(j - 1).getData()) }}
			while (j < set2.size() && !equal) {
				if (node.getData().contentEquals(set2Itr.next().getData())) {
					equal = true;
				}
				j++;
			}
			if (!equal) {
				throw new IllegalArgumentException();
			}
		}
	}
	
	//Get Sorted Nodes test
	
	/**
	 * Tests getSortedNodes()
	 */
	@Test
	public void testGetSortedNodes() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("beta");
		GraphNode<String, String> node3 = new GraphNode<String, String>("alpha");
		set = new HashSet<GraphNode<String, String>>();
		set.add(node1);
		set.add(node3);
		set.add(node2);
		one = new Graph<String, String>(set);
		Set<GraphNode<String, String>> set2 = new TreeSet<GraphNode<String, String>>();
		set2.add(node3);
		set2.add(node2);
		set2.add(node1);
		set = one.getSortedNodes();
		Iterator<GraphNode<String, String>> setItr = set.iterator();
		Iterator<GraphNode<String, String>> set2Itr = set2.iterator();
		// {{ Inv: set2.get(0).getData().equals(set.get(0).getData()),...,
		//	  set2.get(i).getData().equals(set.get(i).getData()) }}
		for (int i = 0; i < set.size(); i++) {
			assertEquals(set2Itr.next().getData(), setItr.next().getData());
		}
	}
	
	//Iterator test
	
	/**
	 * Tests iterator()
	 */
	public void testIterator() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("beta");
		GraphNode<String, String> node3 = new GraphNode<String, String>("alpha");
		set.add(node1);
		set.add(node3);
		set.add(node2);
		one = new Graph<String, String>(set);
		Iterator<GraphNode<String, String>> itr = one.iterator();
		Iterator<GraphNode<String, String>> setItr = set.iterator();
		assertEquals(itr.next().getData(), setItr.next().getData());
		assertEquals(itr.next().getData(), setItr.next().getData());
		assertEquals(itr.next().getData(), setItr.next().getData());
	}
	
	//Get Node test
	
	/**
	 * Tests getNode(
	 */
	public void getNodeTest() {
		GraphNode<String, String> node1 = new GraphNode<String, String>("data");
		GraphNode<String, String> node2 = new GraphNode<String, String>("beta");
		GraphNode<String, String> node3 = new GraphNode<String, String>("alpha");
		set.add(node1);
		set.add(node3);
		set.add(node2);
		one = new Graph<String, String>(set);
		assertEquals(node1, one.getNode("data"));
		assertEquals(node2, one.getNode("beta"));
		assertEquals(node3, one.getNode("alpha"));
		assertEquals(null, one.getNode("omega"));
	}
}
