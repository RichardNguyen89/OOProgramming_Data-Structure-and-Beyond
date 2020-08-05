package roadgraph;

import geography.GeographicPoint;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;


/** create a class to represent nodes in the map based on geographicPoint information.
 * Each node will have a list/hashset to contain information about the edges connecting to it.
 * Because the edges for each note should not be repeated, hashset is a good choice.
 * Also because the exercise states that these are 2 way street, it can be expected that the edgeList of start and end note will contain the other side (2 times on 2 different set)
 * @author linh8
 *
 */
public class MapNode implements Comparable<MapNode> {
	private GeographicPoint location;
	private HashSet<MapEdge> edgeList;
	private double distance;
	private double heurDistance;
	
	public MapNode (GeographicPoint location) {
		this.location = location;
		this.edgeList = new HashSet<MapEdge>();
		this.distance = 0;
		this.heurDistance=0;
	}
	
	public GeographicPoint getLocation() {
		return location;
	}
	
	public HashSet<MapEdge> getEdgeList(){
		return edgeList;
	}
	
	// should be able to add edges into the new list. This edge can be already in form of MapEdge or just from location points
	public void addEdge(MapEdge newEdge) {
		edgeList.add(newEdge);
	}
	
	public void addEdge (GeographicPoint end, String roadName, String roadType, double length) {
		MapEdge newEdge = new MapEdge (location, end, roadName, roadType, length);
		edgeList.add(newEdge);
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance( double d) {
		this.distance = d;
	}
	
	public int compareTo(MapNode node) {
		return Double.compare(this.getDistance(), node.getDistance());
	}
	
	public double getheurDistance() {
		return heurDistance;
	}

	public void setheurDistance(double heurD) {
		this.heurDistance = heurD;
	}
}
