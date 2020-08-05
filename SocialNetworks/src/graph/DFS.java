package graph;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import java.util.HashSet;

public class DFS {
	public static Stack<Integer> DFS(Graph G, Stack<Integer> vertices){
		HashSet<Integer> visited = new HashSet<>();
		Stack <Integer> finished = new Stack<>();
		while (!vertices.isEmpty()) {
			int cur = vertices.pop();
			if(!visited.contains(cur)) {
				DFSVisit(G,cur,visited,finished);
			}
			
		}
		visited.clear();
		return finished;
		
	}
	
	public static void DFSVisit(Graph G, int cur, HashSet<Integer>visited, Stack<Integer>finished) {
		visited.add(cur);
		for(int n: G.exportGraph().get(cur)) {
			if (!visited.contains(n)) {
				DFSVisit(G,n,visited,finished);
			}
		}
		finished.push(cur);
	}
	
	
}
