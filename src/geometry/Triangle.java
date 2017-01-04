package geometry;

import tools.Epsilon;
/**
 * Represents a triangle in 3D space. <b>NOT COMPLETE!</b>
 * @author Donny
 *
 */
public class Triangle implements Primitive{
	Vector3 p, q, t, n;
	
	public Triangle(Vector3 p, Vector3 q, Vector3 t){
		this.p=p;
		this.q=q;
		this.t=t;
		this.n=q.getSubtract(q).cross(t.getSubtract(p));
	}
	public void translate(Vector3 v) {
		p.add(v);
		q.add(v);
		t.add(v);
		
	}

	public void rotateX(double theta, boolean aroundOrigin) {
		Transform transform = Transform.getRotationXInstance(theta);
		this.p=transform.getTransformed(p);
		this.q=transform.getTransformed(q);
		this.t=transform.getTransformed(t);
	}

	public void rotateY(double theta, boolean aroundOrigin) {
		Transform transform = Transform.getRotationYInstance(theta);
		this.p=transform.getTransformed(p);
		this.q=transform.getTransformed(q);
		this.t=transform.getTransformed(t);
	}

	public void rotateZ(double theta, boolean aroundOrigin) {
		Transform transform = Transform.getRotationZInstance(theta);
		this.p=transform.getTransformed(p);
		this.q=transform.getTransformed(q);
		this.t=transform.getTransformed(t);
	}

	public boolean contains(Vector3 v) {
		return false;
	}

	public Intersection intersects(Ray r) {
		Vector3 e1=q.getSubtract(p);
		Vector3 e2=t.getSubtract(p);
		
		Vector3 p = r.getDir().cross(e2);
		double d=e1.dot(p);
		if(!Epsilon.nearlyEquals(d, 0.0))
			return new Intersection();
		
		Vector3 t = r.getOrigin().getSubtract(p);
		double u = t.dot(p)/d;
		if(u<0.0 || u > 1.0)
			return new Intersection();
		
		Vector3 q = t.cross(e1);
		double v = r.getDir().dot(q)/d;
		if(v<0.0 || u+v>1.0)
			return new Intersection();
		
		double w = e2.dot(q)/d;
		if(w > Epsilon.E)
			return new Intersection(true,r.pointOnRay(w),n);
		
		return new Intersection();
	}

	public Vector3 getNormal(Vector3 p) {
		return n;
	}

	public Primitive clone() {
		return new Triangle(p.clone(),q.clone(),t.clone());
	}

	public boolean equals(Primitive prim){
		if(prim instanceof Triangle){
			return true;
			//too lazy to write this code
		}
		return false;
	}

}
