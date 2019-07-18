package campuspathfinder.model.Dijkstras;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import campuspathfinder.model.*;
import campuspathfinder.model.DijkstrasAlgorithm.MalformedDataException;

public class DijkstrasTest {

	private String ernst = "Ernst-the-Bicycling-Wizard";
	private String notkin = "Notkin-of-the-Superhuman-Beard";
	private String perkins = "Perkins-the-Magical-Singing-Instructor";
	private String grossman = "Grossman-the-Youngest-of-them-all";

	@Test
	public void buildGraphTest() throws MalformedDataException {
		Graph<String, Double> graph = DijkstrasAlgorithm.buildGraph("../cse331-19wi-tane1999/src/test/"
				+ "resources/hw7/data/staffSuperheroes.tsv");
		Iterator<GraphNode<String, Double>> itr = graph.getSortedNodes().iterator();
		GraphNode<String, Double> currNode = itr.next();
		
		//Ernst
		assertEquals(ernst, currNode.getData());
		Set<GraphEdge<String, Double>> edges = currNode.getEdges();
		assertEquals(3, edges.size());
		Iterator<GraphEdge<String, Double>> itrEdge = edges.iterator();
		GraphEdge<String, Double> currEdge = itrEdge.next();
		Double cost = 1.0 / 2.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(notkin, currEdge.getNode().getData());
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(grossman, currEdge.getNode().getData());
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(perkins, currEdge.getNode().getData());
		assertFalse(itrEdge.hasNext());
		
		//Grossman
		currNode = itr.next();
		assertEquals(grossman, currNode.getData());
		edges = currNode.getEdges();
		itrEdge = edges.iterator();
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(ernst, currEdge.getNode().getData());
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(notkin, currEdge.getNode().getData());
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(perkins, currEdge.getNode().getData());
		assertFalse(itrEdge.hasNext());

		//Notkin
		currNode = itr.next();
		assertEquals(notkin, currNode.getData());
		edges = currNode.getEdges();
		itrEdge = edges.iterator();
		currEdge = itrEdge.next();
		cost = 1.0 / 2.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(ernst, currEdge.getNode().getData());
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(grossman, currEdge.getNode().getData());
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(perkins, currEdge.getNode().getData());
		assertFalse(itrEdge.hasNext());
		
		//Perkins
		currNode = itr.next();
		assertEquals(perkins, currNode.getData());
		edges = currNode.getEdges();
		itrEdge = edges.iterator();
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(ernst, currEdge.getNode().getData());
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(grossman, currEdge.getNode().getData());
		currEdge = itrEdge.next();
		cost = 1.0 / 1.0;
		assertEquals(cost, currEdge.getLabel());
		assertEquals(notkin, currEdge.getNode().getData());
		assertFalse(itrEdge.hasNext());
	}
	
	@Test
	public void shortestPathTest() {
		Graph<String, Double> graph = new Graph<String, Double>();
		GraphNode<String, Double> node1 = new GraphNode<String, Double>("node1");
		GraphNode<String, Double> node2 = new GraphNode<String, Double>("node2");
		GraphNode<String, Double> node3 = new GraphNode<String, Double>("node3");
		GraphNode<String, Double> node4 = new GraphNode<String, Double>("node4");
		GraphNode<String, Double> node5 = new GraphNode<String, Double>("node5");
		GraphNode<String, Double> node6 = new GraphNode<String, Double>("node6");
		
		GraphEdge<String, Double> edge1 = new GraphEdge<String, Double>(4.0, node2);
		GraphEdge<String, Double> edge2 = new GraphEdge<String, Double>(5.0, node3);
		node1.addEdge(edge1);
		node1.addEdge(edge2);
		GraphEdge<String, Double> edge3 = new GraphEdge<String, Double>(6.0, node4);
		GraphEdge<String, Double> edge4 = new GraphEdge<String, Double>(4.0, node5);
		node2.addEdge(edge3);
		node3.addEdge(edge4);
		GraphEdge<String, Double> edge5 = new GraphEdge<String, Double>(4.0, node6);
		GraphEdge<String, Double> edge6 = new GraphEdge<String, Double>(3.0, node6);
		node4.addEdge(edge5);
		node5.addEdge(edge6);
		
		graph.addNode(node1);
		graph.addNode(node2);
		graph.addNode(node3);
		graph.addNode(node4);
		graph.addNode(node5);
		graph.addNode(node6);
		//should get edge2, edge4, edge5
		List<GraphEdge<String, Double>> path = DijkstrasAlgorithm.shortestPath(graph, "node1", "node6");
		assertEquals(3, path.size());
		Iterator<GraphEdge<String, Double>> itr = path.iterator();
		GraphEdge<String, Double> edge = itr.next();
		assertEquals(edge2.getLabel(), edge.getLabel());
		edge = itr.next();
		assertEquals(edge4.getLabel(), edge.getLabel());
		edge = itr.next();
		assertEquals(edge6.getLabel(), edge.getLabel());
		
		
	}
	
	@Test
	public void shortestPathToSelfTest() throws MalformedDataException {
		Graph<String, Double> graph = DijkstrasAlgorithm.buildGraph("../Campus-Paths-Finder/app/src/test/"
				+ "resources/hw7/data/shortestPathTest.tsv");
		List<GraphEdge<String, Double>> path = DijkstrasAlgorithm.shortestPath(graph, ernst, ernst);
		assertFalse(path == null);
		assertEquals(0, path.size());
	}
	
	@Test
	public void shortestPathUnconnectedTest() throws MalformedDataException {
		Graph<String, Double> graph = DijkstrasAlgorithm.buildGraph("../Campus-Paths-Finder/app/src/test/"
				+ "resources/hw7/data/shortestPathTest.tsv");
		List<GraphEdge<String, Double>> path = DijkstrasAlgorithm.shortestPath(graph, ernst, "unconnected");
		assertEquals(null, path);
	}
}
