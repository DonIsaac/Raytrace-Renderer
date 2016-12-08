package geometry;

/**
 * Represents an object that can be transformed (i.e: moved/rotated, usually in
 * world space. Most Transformable objects will use a {@link Transform} matrix,
 * but it is not required. If you are unfamiliar with the intended use of these
 * functions, read up on geometric transformations.
 * 
 * @author Donny
 *
 */
public interface Transformable {
	/**
	 * Translates the object.
	 * 
	 * @param v
	 *            The amount to transfer the object along each axis
	 */
	public void translate(Vector3 v);

	/**
	 * Rotates the object around the X-axis. This means that if your object is
	 * translated off the X-axis, it will move in a circle around it, not around
	 * it's local origin, unless <b>aroundCenter</b> is set to true.)
	 * 
	 * @param theta The amount to rotate the object in radians.
	 * @param aroundOrigin Whether or not the rotation should be around the object's local origin.
	 */
	public void rotateX(double theta, boolean aroundOrigin);
	/**
	 * Rotates the object around the Y-axis. This means that if your object is
	 * translated off the Y-axis, it will move in a circle around it, not around
	 * it's local origin, unless <b>aroundCenter</b> is set to true.)
	 * 
	 * @param theta The amount to rotate the object in radians.
	 * @param aroundOrigin Whether or not the rotation should be around the object's local origin.
	 */
	public void rotateY(double theta, boolean aroundOrigin);
	/**
	 * Rotates the object around the Z-axis. This means that if your object is
	 * translated off the Z-axis, it will move in a circle around it, not around
	 * it's local origin, unless <b>aroundCenter</b> is set to true.)
	 * 
	 * @param theta The amount to rotate the object in radians.
	 * @param aroundOrigin Whether or not the rotation should be around the object's local origin.
	 */
	public void rotateZ(double theta, boolean aroundOrigin);
}
