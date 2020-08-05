/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;
import util.GraphLoader;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	// Ver = integer, hashset used to store out vertices
	private HashMap<Integer, HashSet<Integer>> cMap;
	// Ver = integer, hashset used to store in vertices
	private HashMap<Integer, HashSet<Integer>> verticesIn;
	private HashSet<Edge> edges;
	
	private int numVertices;
	private int numEdges;
	
	public CapGraph()
	{
		cMap = new HashMap<Integer, HashSet<Integer>>();
		verticesIn = new HashMap<Integer, HashSet<Integer>>();
		edges = new HashSet<Edge>();
		numVertices = 0;
	}
	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		if(!cMap.containsKey(num)) {
			cMap.put(num, new HashSet<Integer>());
			numVertices++;
		}
		// TODO Auto-generated method stub
		
		if (!verticesIn.containsKey(num)) {
			verticesIn.put(num, new HashSet<Integer>());
			
		}
		
		
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		if (cMap.containsKey(from) && cMap.containsKey(to)) {
			cMap.get(from).add(to);
			numEdges++;
			Edge e = new Edge(from, to);
			edges.add(e);
		}
		verticesIn.get(to).add(from);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		
		//pseudo code first
		// it will return a cMap of center and friends and edges connect those friends as well
		// create a new cMap
		Graph egoNet = new CapGraph();
		// addVertex(center)
		egoNet.addVertex(center);
		// for each of the friend, add them and the edge connect to center to the graph
		// take out the common friends and add them and the edges to the graph. 
		// Check the 
		for (int f1: cMap.get(center)) {
			egoNet.addVertex(f1);
			egoNet.addEdge(center, f1);
			List<Integer> common = new ArrayList<>(cMap.get(f1));
			common.retainAll(cMap.get(center));
			
			for (int f2: common) {
				egoNet.addVertex(f2);
				egoNet.addEdge(f1, f2);
			}
			
			if (cMap.get(f1).contains(center)) {// the above common will exclude the center. Therefore, need to check this case as well
				egoNet.addEdge(f1, center);
			}
		}
		
	return egoNet;
	}

	public Graph reverseEdges (HashMap<Integer, HashSet<Integer>> map) {
		Graph rG = new CapGraph();
		for (int n : map.keySet()) {
			rG.addVertex(n);
			for (int j : map.get(n)) {
				rG.addVertex(j);
				//rG.exportGraph().get(j).add(n); //because this is a directed graph, sometimes the original has n--> j but not j-->n. when reverse, need to make sure add n so that can form j-->n
				rG.addEdge(j, n);
			}
		}
		
		
	return rG;	
	}
	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub
		List<Graph> SCCs = new ArrayList<>();
		Stack<Integer> start = new Stack<>();
		start.addAll(cMap.keySet());
		// DFS first time. keep track of the order
		Stack<Integer> finishedOrder = DFS.DFS(this, start);
		//System.out.print(finishedOrder);	
		// reverse the graph
		Graph reverse = reverseEdges(cMap);
		
		// dfs the second time on the reverse graph based on the finished order
		//Stack<Integer> fnal = DFS.DFS(reverse, finishedOrder);
		
		HashSet<Integer> visited = new HashSet<>();
		Stack<Integer> finished = new Stack<>();
		
		while (!finishedOrder.isEmpty()) {
			int temp = finishedOrder.pop();
			if (!visited.contains(temp)) {
				DFS.DFSVisit(reverse, temp, visited, finished);
				if (finished.size()>0) {
					Graph tempGraph = new CapGraph();
					for (int n : finished) {
						tempGraph.addVertex(n);
					}
					finished = new Stack<>();
					SCCs.add(tempGraph);
				}
			}
		}
		
		// return a list of SCC
		
		return SCCs;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub
		for(Integer v : cMap.keySet()) {
			System.out.print(v+": ");
			StringBuffer neighbors = new StringBuffer();
			for(Integer n : cMap.get(v)) {
				neighbors.append(n+" ");
			}
			System.out.println(neighbors);
		}
		System.out.println("numVertices: "+this.numVertices+"\tnumEdges: "+numEdges);
		return cMap;
	
	}
	

	
	/* return a list of potential friends who have connections with other friends of the user
	 * 
	 * 
	 */
	public HashMap<Integer, HashSet<Integer>> potentialFriendlvl1(int user){
		HashMap<Integer, HashSet<Integer>> pF = new HashMap<>();
		
		// get the friends of user
		HashSet<Integer> friend = new HashSet<>(cMap.get(user));
		
		// take all other users
		
		HashSet<Integer> otherUsers = new HashSet<>(cMap.keySet());
		otherUsers.removeIf(p -> p == user); // remove user
		// for each, check their friends.
		for (int o : otherUsers) {
			if (!friend.contains(o)) {
				HashSet<Integer> common = new HashSet<>(cMap.get(o));
				common.retainAll(friend); //retain the common. if the common size > 0;
				if (common.size()>0) {
					pF.put(o, common); // the potential friends and the connections.
				}
			}
		}
		
		return pF; 
	}	
		
	
	
	/* bfs helper
	 * 
	 */
	
	public boolean bfsSearch (Integer start, Integer goal, HashMap<Integer, Integer> parent) {
		boolean found = false;
		
		
		Queue<Integer> toVisit = new LinkedList<>();
		HashSet<Integer> visited = new HashSet<>();
		toVisit.add(start);
		
		while(!toVisit.isEmpty()) {
			int cur = toVisit.poll();
			if (cur == goal) {
				found = true;
				break;
			}
			for (int neighbour : cMap.get(cur)) {
				if (!visited.contains(neighbour)) {
					visited.add(neighbour);
					parent.put(neighbour, cur);
					toVisit.add(neighbour);
				}
			}
		}
		return found;
	}
	
	
	public List<Integer> constructPath(Integer start, Integer goal, HashMap<Integer, Integer>parent){
		LinkedList<Integer> path = new LinkedList<>();
		
		Integer cur = goal;
		while(!cur.equals(start)) {
			path.addFirst(cur);
			
			Edge e = findEdge(parent.get(cur),cur);
			e.setBetweenness(e.getBetweenness()+1);
			
			cur = parent.get(cur);
		}
		return path;
	}
	
	public Edge findEdge(Integer from, Integer to) {
		for (Edge e: edges) {
			if(e.getNumFrom() == from && e.getNumTo() ==to) {
				return e;
			}
		}
		return null;
	}
	
	
	
	/*bfs
	 * 
	 */
	public List<Integer> bfs(Integer start, Integer goal, CapGraph g){
		HashMap<Integer, Integer> parent = new HashMap<>();
		if(!bfsSearch(start, goal, parent)) {
			return null;
		}
			
		return constructPath(start, goal, parent);
	}
	
	/* find the highest betweeness edge
	 * 
	 */
	
	public Edge getHighestBetweeness (CapGraph g) {
		int from=0;
		int to=0;
		int max = 0;
		for (Edge e : g.edges) {
			int b = e.getBetweenness();
			if (b > max) {
				from = e.getNumFrom();
				to = e.getNumTo();
				max = b;
			}
		}
		return g.findEdge(from, to);
	}
	
	/* remove edge
	 * 
	 */
	
	public void remove (Integer from, Integer to) {
		if(cMap.get(from).contains(to)) {
			cMap.get(from).remove(to);
			this.numEdges--;
			edges.remove(this.findEdge(from,to));
		}
		verticesIn.get(to).remove(from);
	}
	
	/* implement the communities algorithms from the video
	 * 
	 */
		
public CapGraph getCommunities(int numCommunities) {
		
		int splits = 1;
		
		CapGraph g = new CapGraph();
		for(Integer v : this.cMap.keySet()) {
			g.addVertex(v);
			for(Integer neighbor : cMap.get(v)) {
				g.addVertex(neighbor);
				g.addEdge(v, neighbor);
			}
		}
		
		// Do until we reach number of desired communities, or until there are no more edges
		while(splits < numCommunities && g.numEdges > 0) {
			
			for (Integer v : g.cMap.keySet()) {
				for (Integer other : g.cMap.keySet()) {
					if(!other.equals(v)) {

						g.bfs(v, other, g);
					}
					
				}
			}
			
			// Find and remove the Edge with the highest betweenness
			Edge e = g.getHighestBetweeness(g);
			int from = e.getNumFrom();
			int to = e.getNumTo();
			
			
			g.remove(from, to);
						
			splits++;
		}
		
		//while(!rootNodes.isEmpty()) {
			//communities.add(g.getNodeReach(rootNodes.pop(), g.numVertices));	
		//}
		
		return g;
	}
	
	
	
	public static void main (String[] args) {
		long startTime, endTime, duration;
		
		
		CapGraph testGraph = new CapGraph();
		
		System.out.print("Loading graph...");
		startTime = System.nanoTime();
		GraphLoader.loadGraph(testGraph, "data/test1.txt");
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;
		System.out.print(" DONE: "+duration+" ms\n");
		
		startTime = System.nanoTime();
		HashMap<Integer, HashSet<Integer>> a = testGraph.potentialFriendlvl1(12);
		System.out.println(a.keySet());
		testGraph.exportGraph();

		
		
		System.out.println("BFS:"+  testGraph.bfs(1, 5,testGraph));
		//System.out.println(testGraph.findEdge(1, 2));
		//System.out.println(testGraph.findEdge(0, 3));
		System.out.println("E: " + testGraph.getHighestBetweeness(testGraph));
		testGraph.getCommunities(5).exportGraph();
		//testGraph.remove(0, 2);
		System.out.println(testGraph.cMap.get(0));
		System.out.println(testGraph.verticesIn.get(0));
		
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000;
		System.out.print("Runtime: "+duration+" ms\t");
		

	}

}

