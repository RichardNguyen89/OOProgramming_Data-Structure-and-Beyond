/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	//TODO: Add your member variables here in WEEK 3
	// a map to keep the geographical points and map point. The map point should have edges as well.
	
	private HashMap <GeographicPoint, MapNode> nodeMap; 
	private HashSet <MapEdge> edgeList;
	private HashMap <MapNode,Double> distanceTo;
	public static final int DEFAULT_SPEED_LIMIT = 50;
	public static final int DEFAULT_HIGHWAY_SPEED_LIMIT = 100;
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		// TODO: Implement in this constructor in WEEK 3
		nodeMap = new HashMap<>();
		edgeList = new HashSet<>();
		distanceTo = new HashMap<>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		//TODO: Implement this method in WEEK 3
		return nodeMap.values().size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		//TODO: Implement this method in WEEK 3
		Set <GeographicPoint> V = nodeMap.keySet();
		return V;
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		//TODO: Implement this method in WEEK 3
		return edgeList.size();
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		// TODO: Implement this method in WEEK 3
		if (nodeMap.containsKey(location)|| location == null) {
			return false;
		} else {nodeMap.put(location, new MapNode(location));
		return true;
		}
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		if (length <= 0 ) {throw new IllegalArgumentException();}
		if (nodeMap.get(from) == null || nodeMap.get(to) == null || roadName == null  || roadType == null) {
			throw new IllegalArgumentException();
		}
		nodeMap.get(from).addEdge(to, roadName, roadType, length);
		edgeList.add(new MapEdge(from, to, roadName, roadType, length));
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3
		if (start == null || goal == null) { throw new IllegalArgumentException();}
		// hashmap parent map to keep track of parent and current node. Helpful to reconstruct the path
		HashMap<MapNode,MapNode> parent = new HashMap<>();
		
		// bfs. queue and visited to keep track of nodes
		Queue<MapNode> nodesToBeVisited = new LinkedList<>();
		HashSet<MapNode> nodesVisited = new HashSet<>();
		
		MapNode current = null;
		MapNode startNode = nodeMap.get(start);
		MapNode endNode = nodeMap.get(goal);
		MapNode neighbour = null;
		if (startNode == null || endNode == null) {
			System.out.println("Illegal");
			return null;
		}
		
		nodesToBeVisited.add(startNode);
		nodeSearched.accept(startNode.getLocation()); // hook for visualization
		while (!nodesToBeVisited.isEmpty()) {
			current = nodesToBeVisited.remove();
			nodeSearched.accept(current.getLocation());
			if (current.equals(endNode)) { // check to see if reach goal
				return constructPath(parent, endNode, startNode);
			}
			for (MapEdge e: current.getEdgeList()) {
				neighbour = nodeMap.get(e.getEnd()); // get the other nodes that connect to current nodes via searching the edgeList
				if (!nodesVisited.contains(neighbour)) {
					nodesVisited.add(neighbour);
					nodesToBeVisited.add(neighbour);
					parent.put(neighbour, current);
				}
			}
		}
		
		
		return null;
		
		// Hook for visualization.  See writeup.
		
		//nodeSearched.accept(next.getLocation());

		
	}
	
	/** retrace the hashmap parent to construct the path
	 * 
	 * @param parent
	 * @param endNode
	 * @param startNode
	 * @return
	 */
	public List<GeographicPoint> constructPath(HashMap<MapNode, MapNode> parent, MapNode endNode, MapNode startNode){
		List<GeographicPoint> route = new LinkedList<>();
		MapNode current = endNode;
		
		while (!current.equals(startNode)) {
			// addFirst to make sure the normal order
			((LinkedList<GeographicPoint>) route).addFirst(current.getLocation());
			current = parent.get(current);
		}
		
		
		
		
		
		return route;
	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 4

		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		PriorityQueue <MapNode> PQ = new PriorityQueue<>();
		HashSet <MapNode> Visited = new HashSet<>();
		HashMap <MapNode, MapNode> parent = new HashMap<>();
		
		// set distance max integer for all nodes
		for (GeographicPoint n: nodeMap.keySet()) {
			nodeMap.get(n).setDistance(Integer.MAX_VALUE);
			
		}
		
		// implement dijkstra
		int count =0;
		MapNode current = null;
		MapNode startNode = nodeMap.get(start);
		MapNode endNode = nodeMap.get(goal);
		MapNode neighbour = null;
		startNode.setDistance(0);
		PQ.add(startNode);
		if (startNode == null || endNode == null) {
			System.out.println("Illegal");
			return null;
		}
		
		
		nodeSearched.accept(startNode.getLocation()); // hook for visualization
		while (!PQ.isEmpty()) {
			current = PQ.remove();
			count++;
			nodeSearched.accept(current.getLocation());
			if (!Visited.contains(current)) {
				Visited.add(current);
				if (current.equals(endNode)) { // check to see if reach goal
					System.out.print("D: " + count);
					return constructPath(parent, endNode, startNode);
				}
			}
			
			for (MapEdge e: current.getEdgeList()) {
				neighbour = nodeMap.get(e.getEnd()); // get the other nodes that connect to current nodes via searching the edgeList
				if (!Visited.contains(neighbour)) {
					if(neighbour.getDistance()/DEFAULT_HIGHWAY_SPEED_LIMIT > (e.getLength()+current.getDistance())/ DEFAULT_HIGHWAY_SPEED_LIMIT) {
						neighbour.setDistance( e.getLength()+current.getDistance()); //update shorter distance // new version use time so have to update time
						PQ.add(neighbour);
						parent.put(neighbour, current);
					}
					
				}
			}
		}
		
		
		return null;
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 4
		
		// Hook for visualization.  See writeup.
		//nodeSearched.accept(next.getLocation());
		
		PriorityQueue <MapNode> PQ = new PriorityQueue<>();
		HashSet <MapNode> Visited = new HashSet<>();
		HashMap <MapNode, MapNode> parent = new HashMap<>();
		
		// set distance max integer for all nodes
		for (GeographicPoint n: nodeMap.keySet()) {
			nodeMap.get(n).setDistance(Integer.MAX_VALUE);
			nodeMap.get(n).setheurDistance(Integer.MAX_VALUE);
		}
		
		// implement A*
		int count = 0;
		MapNode current = null;
		MapNode startNode = nodeMap.get(start);
		MapNode endNode = nodeMap.get(goal);
		MapNode neighbour = null;
		startNode.setDistance(0);
		PQ.add(startNode);
		if (startNode == null || endNode == null) {
			System.out.println("Illegal");
			return null;
		}
		
		
		nodeSearched.accept(startNode.getLocation()); // hook for visualization
		while (!PQ.isEmpty()) {
			current = PQ.remove();
			count++;
			nodeSearched.accept(current.getLocation());
			if (!Visited.contains(current)) {
				Visited.add(current);
				if (current.equals(endNode)) { // check to see if reach goal
					System.out.print("A: " + count);
					return constructPath(parent, endNode, startNode);
				}
			}
			
			for (MapEdge e: current.getEdgeList()) {
				neighbour = nodeMap.get(e.getEnd()); // get the other nodes that connect to current nodes via searching the edgeList
				if (!Visited.contains(neighbour)) {
					double straightDistance = neighbour.getLocation().distance(goal);
					if(neighbour.getDistance() > (e.getLength()+current.getDistance()+straightDistance)) {
						neighbour.setDistance( e.getLength()+current.getDistance() + straightDistance); //update shorter distance
						PQ.add(neighbour);
						parent.put(neighbour, current);
					}
					
				}
			}
		}
		
		
		return null;
	}

	
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		
		/* Use this code in Week 3 End of Week Quiz */
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		
		
	}
	
}
