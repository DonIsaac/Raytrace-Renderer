package geometry;

import tools.Epsilon;

public class Sphere implements Primitive {
	protected Vector3 c;
	protected double r;

	public Sphere(Vector3 pos, double radius) {
		c = pos;
		r = radius;
	}

	public boolean contains(Vector3 point) {

		return c.distFrom(point) <= r;
	}

	/**
	 * <style> table, th, td{ border: 1px solid black; border-collapse:
	 * collapse; } th,td{ padding:1px; } </style> Equation of a line: L = o+td
	 * </br> Equation of a sphere: (p-c)^2 = r^2 </br>
	 * <table>
	 * <tr>
	 * <td>(o+td-c)^2 = r^2</td>
	 * <td>Substitute the equation of a line for the point in the equation of a
	 * sphere</td>
	 * </tr>
	 * <tr>
	 * <td>t^2</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td></td>
	 * <td></td>
	 * </tr>
	 * </table>
	 * ------------------------------------
	 */
	public Intersection intersects(Ray ray) {
		Vector3 p = ray.getOrigin().getSubtract(c);
		// final double B = 2 * ray.getDir().dot(P);
		// final double C = P.dot(P) - r * r;
		// final double discrim = B * B - 4 * C;
		Vector3 l = ray.getDir().clone();
		double b = l.dot(p);
		final double discrim = b * b - l.getSquared()
				* (p.getSquared() - r * r);
		if (discrim < 0) {
			return new Intersection(false);
		} else if (Epsilon.nearlyEquals(discrim, 0.0)) {
			double t = -b + Math.sqrt(discrim);
			Vector3 hit = c.getAdd(ray.pointOnRay(t).getSubtract(c).getNormalized().getScale(r));
			//return new Intersection(true, ray.pointOnRay(t));
			return new Intersection(true,hit);
		} else {
			double t1 = -b + Math.sqrt(discrim);
			double t2 = -b - Math.sqrt(discrim);
			if (Epsilon.nearlyEquals(t2, 0.0)) {
				Vector3 hit = c.getAdd(ray.pointOnRay(t1).getSubtract(c).getNormalized().getScale(r));
				return new Intersection(true, hit);
			}
			// System.out.println("two hits, "+t1+" "+t2);
			Vector3 hit1 = c.getAdd(ray.pointOnRay(t1).getSubtract(c).getNormalized().getScale(r));
			Vector3 hit2 = c.getAdd(ray.pointOnRay(t2).getSubtract(c).getNormalized().getScale(r));
			return new Intersection(true, hit1,
					hit2);
		}

	}

	public Vector3 getNormal(Vector3 p) {
		return p.getSubtract(c).getNormalized();
	}
	public Vector3 getCenter(){
		return c;
	}
	public void setCenter(Vector3 center){
		this.c=center;
	}
	public double getRadius(){
		return r;
	}
	public void setRadius(double radius){
		this.r=radius;
	}

	@Override
	public Sphere clone() {
		return new Sphere(c.clone(), r);
	}

	@Override
	public String toString(){
		return "C"+c+" r:"+r;
	}

	public boolean equals(Primitive prim){
		if(!(prim instanceof Sphere))
			return false;
		return c.equals(((Sphere)prim).getCenter()) && Epsilon.nearlyEquals(r, ((Sphere)prim).getRadius());
	}

	public void translate(Vector3 v) {
		this.c.add(v);
		
	}

	public void rotateX(double theta, boolean aroundOrigin) {
		//Rotating a sphere does nothing!
	}

	public void rotateY(double theta, boolean aroundOrigin) {
		//Rotating a sphere does nothing!
	}

	public void rotateZ(double theta, boolean aroundOrigin) {
		//Rotating a sphere does nothing!
	}
}
