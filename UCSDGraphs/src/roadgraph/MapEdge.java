package roadgraph;

import geography.GeographicPoint;
import java.util.Set;
import java.util.List;

/** create an object to represent edges in the map.
 * The edge should contain locations of points as well as other information such as roadName, roadType and length
 * Also added get and set method to change private fields in the class.
 * @author linh8
 *
 */
public class MapEdge {

	private GeographicPoint start;
	private GeographicPoint end;
	private String roadName;
	private String roadType;
	private double length;
	
	public MapEdge (GeographicPoint start, GeographicPoint end, String roadName, String roadType, double length) {
		this.start = start;
		this.end = end;
		this.roadName = roadName;
		this.roadType = roadType;
		this.length = length;
	}
	
	public GeographicPoint getStart() {
		return start;
	}
	
	public void setStart(GeographicPoint point1) {
		this.start = point1;
	}
	public GeographicPoint getEnd() {
		return end;
	}
	
	public void setEnd(GeographicPoint point2) {
		this.end = point2;
	}
	
	public String getStreetName() {
		return roadName;
	}
	
	public void setStreetName(String streetName) {
		this.roadName = streetName;
	}
	
	public String getStreetType() {
		return roadType;
	}
	
	public void setStreetType(String streetType) {
		this.roadType = streetType;
	}
	
	public double getLength() {
		return length;
	}
	
	public void setStreetLength(double distance) {
		this.length = distance;
	}
	
	
	
}
