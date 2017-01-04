package geometry;

import java.util.ArrayList;

/**
 * Stores information about a {@link Ray}-Geometry intersection. This class is
 * effectively a struct.
 * 
 * @author Donny
 *
 */
public class Intersection {
	/** The point in world space where there was an intersection. */
	public Vector3 hit;
	/** The {@link Vector3} normal to the intersected surface. **/
	public Vector3 normal;
	/**
	 * True if there was an intersection, false otherwise.<br/><br/><i>(NOTE: I know that I
	 * could use a method that checks the status of the intersections array
	 * instead of separately setting an isHit value, but there were bugs
	 * originally and this way works.</i>
	 */
	public boolean isHit;
	/**
	 * Creates a new {@link Intersection} struct that stores the points of intersection.
	 * @param isHit was there an intersection?
	 * @param intersections list of intersections in world space
	 */
	public Intersection(boolean isHit, Vector3 intersection, Vector3 normal) {
		this.isHit = isHit;
		this.hit = intersection;
		this.normal=normal;
	}
	/**
	 * Quick Constructor for when there is no intersection.
	 */
	public Intersection(){
		this.isHit=false;
		this.hit=Vector3.ZERO;
		this.normal=Vector3.ZERO;
	}
	
}
