package geometry;

/**
 * Stores information about a {@link Ray}-Geometry intersection. This class is
 * effectively a struct.
 * 
 * @author Donny
 *
 */
public class Intersection {
	/** Points in world space where there was an intersection. */
	public Vector3[] intersections;
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
	public Intersection(boolean isHit, Vector3... intersections) {
		this.isHit = isHit;
		this.intersections = intersections;
	}
	/**
	 * Gets the closest intersection point relative to a point.
	 * @param p the point to check
	 * @return the closest intersection to <b>p</b>
	 */
	public Vector3 getClosestIntersection(Vector3 p) {
		if (intersections.length == 0)
			return new Vector3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		if (intersections.length == 1) {
			return intersections[0];
		}
		Vector3 closest = intersections[0];
		for (Vector3 v : intersections) {
			if (p.distFrom(v) < p.distFrom(closest))
				closest = v.clone();
		}
		return closest;

	}
}
