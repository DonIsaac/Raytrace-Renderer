package geometry;
/**
 * Interface used by all primitive shapes.
 * @author Donny
 *
 */
public interface Primitive extends Transformable{
	/**
	 * Checks to see if a point represented by a {@link Vector3} is inside the {@link Primitive}
	 * @param v the point to check
	 * @return
	 */
	public boolean contains(Vector3 v);
	/**
	 * Checks for intersections between the Primitive and a {@link Ray}.
	 * @param r The {@link Ray} to test
	 * @return an {@link Intersection} struct containing the intersection information
	 */
	public Intersection intersects(Ray r);
	/**
	 * Gets the normal {@link Vector3} at a point in world space.
	 * @param p The point to check
	 * @return the normal {@link Vector3}
	 */
	public Vector3 getNormal(Vector3 p);
	public Primitive clone();
	/**
	 * Checks to see if this {@link Primitive} is equal to a different {@link Primitive}.
	 * @param prim The {@link Primitive} to compare
	 * @return <code>true</code> if they are equal, <code>false</code> otherwise.
	 */
	public boolean equals(Primitive prim);
}
