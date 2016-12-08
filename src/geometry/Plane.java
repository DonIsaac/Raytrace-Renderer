package geometry;

import tools.Epsilon;

/**
 * Represents a geometric plane in 3D space.
 * 
 * @author Donny
 *
 */
public class Plane implements Primitive {
	/** A point on the plane that is used for position*/
	protected Vector3 p;
	/** The normal vector that is used for orientation.*/
	protected Vector3 n;

	/**
	 * Constructs a plane using a point in space and a normal vector.
	 * @param point A point on the plane
	 * @param normal The plane's normal vector
	 */
	public Plane(Vector3 point, Vector3 normal) {
		p = point;
		n = normal;
		n.nor();

	}
	/**
	 * Constructs a plane using three points in space.
	 * @param p The first point
	 * @param q The second point
	 * @param r The third point
	 */
	public Plane(Vector3 p, Vector3 q, Vector3 r) {
		Vector3 pq = q.getSubtract(p);
		Vector3 pr = r.getSubtract(p);
		p = p.clone();
		n = pq.cross(pr);
		n.nor();

	}
	/**
	 * 
	 * @return The point on the plane. Used for location.
	 */
	public Vector3 getPoint() {
		return p;
	}
	/**
	 * Sets the point on the plane. Used for location.
	 * @param point The new point
	 */
	public void setPoint(Vector3 point) {
		this.p = point;
	}

	public Vector3 getNormal(Vector3 v) {
		return n;
	}

	public Vector3 getNormal() {
		return n;
	}
	/**
	 * Sets the normal vector.
	 * @param n The new normal vector
	 */
	public void setNormal(Vector3 n) {
		this.n = n;
		n.nor();
	}

	public boolean contains(Vector3 v) {
		return Epsilon.nearlyEquals(n.dot(v.getSubtract(p)), 0.0);
	}

	public Intersection intersects(Ray r) {
		if (Epsilon.nearlyEquals(n.dot(r.getDir()), 0.0))
			return new Intersection(false);
		double t = n.dot(p.getSubtract(r.getOrigin())) / n.dot(r.getDir());
		if (t > 0)
			return new Intersection(true, r.pointOnRay(t));
		else
			return new Intersection(false);
	}

	@Override
	public Plane clone() {
		return new Plane(p.clone(), n.clone());
	}

	public boolean equals(Primitive prim) {
		if (!(prim instanceof Plane))
			return false;
		boolean nEqual = this.n.equals(((Plane) prim).getNormal());
		boolean pEqual = Epsilon.nearlyEquals(n.dot(p), ((Plane) prim).getNormal().dot(((Plane) prim).getPoint()));
		return nEqual && pEqual;
	}

	public void translate(Vector3 v) {
		this.p.add(v);

	}

	public void rotateX(double theta, boolean aroundOrigin) {
		Transform t = Transform.getRotationXInstance(theta);
		this.n = t.getTransformed(n);
		if (!aroundOrigin)
			this.p = t.getTransformed(p);
	}

	public void rotateY(double theta, boolean aroundOrigin) {
		Transform t = Transform.getRotationYInstance(theta);
		this.n = t.getTransformed(n);
		if (!aroundOrigin)
			this.p = t.getTransformed(p);
	}

	public void rotateZ(double theta, boolean aroundOrigin) {
		Transform t = Transform.getRotationZInstance(theta);
		this.n = t.getTransformed(n);
		if (!aroundOrigin)
			this.p = t.getTransformed(p);
	}

}
