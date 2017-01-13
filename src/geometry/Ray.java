package geometry;
/**
 * Represents a ray in 3D space.
 * @author Donny
 *
 */
public class Ray {
	/** The Ray's origin */
	private Vector3 origin;
	/** The Ray's direction */
	private Vector3 dir;
	/**
	 * Full constructor. Creates a ray starting at an origin point that has a specified direction.
	 * @param origin The origin (where it starts)
	 * @param direction The direction (where it points)
	 */
	public Ray(Vector3 origin, Vector3 direction){
		this.origin=origin;
		this.dir=direction;
		this.dir.nor();
	}
	/**
	 * Creats a ray that has an origin centered at the world origin.
	 * @param direction The direction (where it points)
	 */
	public Ray(Vector3 direction){
		this.origin = Vector3.ZERO;
		this.dir=direction;
	}
	/**
	 * Creates a Ray that intersects two points.
	 * @param u The first point
	 * @param v The second point
	 * @return the Ray
	 */
	public static Ray createRayFromPoints(Vector3 u, Vector3 v){
		Vector3 dir = v.getSubtract(u);
		return new Ray(u.clone(),dir);
	}
	/**
	 * Gets a point on the Ray that is a specified distance from the origin
	 * @param x The distance from the origin
	 * @return The point on the Ray
	 */
	public Vector3 pointOnRay(double x){
		return origin.getAdd(dir.getScale(x));
	}
	/**
	 * 
	 * @return The Ray's origin
	 */
	public Vector3 getOrigin() {
		return origin;
	}
	/**
	 * Sets the Ray's origin
	 * @param origin The new origin
	 */
	public void setOrigin(Vector3 origin) {
		this.origin = origin;
	}
	/**
	 * 
	 * @return The Ray's Direction
	 */
	public Vector3 getDir() {
		return dir;
	}
	/**
	 * Sets the Ray's direction
	 * @param dir The new direction
	 */
	public void setDir(Vector3 dir) {
		this.dir = dir;
		this.dir.nor();
	}
	public boolean equals(Ray ray){
		return this.origin.equals(ray.origin)&&this.dir.equals(ray.dir);
	}
	@Override
	public String toString(){
		return "<"+origin.x+","+origin.y+","+origin.z+">+ t<"+dir.x+","+dir.y+","+dir.z+">";
	}
}
