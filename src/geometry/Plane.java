package geometry;

import java.awt.Color;

import lighting.Material;
import tools.Epsilon;

public class Plane implements Primitive {

	protected Vector3 p, n;

	public Plane(Vector3 point, Vector3 normal) {
		p = point;
		n = normal;
		n.normalize();

	}

	public Plane(Vector3 p, Vector3 q, Vector3 r) {
		Vector3 pq = q.getSubtract(p);
		Vector3 pr = r.getSubtract(p);
		p = p.clone();
		n = pq.cross(pr);
		n.normalize();

	}

	public Vector3 getPoint() {
		return p;
	}

	public void setPoint(Vector3 point) {
		this.p = point;
	}

	public Vector3 getNormal(Vector3 v) {
		return n;
	}

	public Vector3 getNormal() {
		return n;
	}

	public void setNormal(Vector3 n) {
		this.n = n;
		n.normalize();
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
