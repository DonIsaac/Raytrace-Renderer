package model;

import java.util.ArrayList;

import geometry.Intersection;
import geometry.Primitive;
import geometry.Ray;
import geometry.Sphere;
import geometry.Transform;
import geometry.Vector3;
import lighting.Material;
import tools.Epsilon;

/**
 * Re
 * 
 * @author Donny
 *
 */
public class Model implements IModel {

	protected Model() {

	}

	protected Material mat;
	protected ArrayList<Vector3> verticies = new ArrayList<Vector3>();
	protected ArrayList<Vector3> normals = new ArrayList<Vector3>();
	protected ArrayList<Face> faces = new ArrayList<Face>();

	protected Transform matrix;
	protected Sphere boundingSphere;

	public Material getMaterial() {
		return mat;
	}

	public Intersection intersects(Ray r) {
		/*
		 * There was a problem with the intersection algorithm that caused it to
		 * load the expected triangle, plus another one attached to it. I
		 * noticed that when you rotated the points used in the algorithm (v1
		 * becomes v2, v2 becomes v3, v3 becomes v1), it did the same thing,
		 * except that the attached triangle was attached in a different place.
		 * Because of this, instead of finding a proper fix, I decided to run
		 * the intersection algorithm twice, once with v1, v2, v3 and once with
		 * v2, v3, v1. This way, if it only hits once, I know that there was an
		 * intersection with the extranious triangle and not an actual hit.
		 * WOOOO lazy solutions!
		 */
		// test intersection with bounding sphere to improve efficency
		Intersection i = boundingSphere.intersects(r);
		if (!i.isHit)
			return new Intersection();

		Vector3 hit = Vector3.ZERO.clone(), normal = Vector3.ZERO.clone();
		double dist = Double.MAX_VALUE;
		boolean isHit = false;

		for (Face f : faces) {
			Vector3 v1 = matrix.getTransformed(verticies.get((int) (f.vertex.x - 1)));
			Vector3 v2 = matrix.getTransformed(verticies.get((int) (f.vertex.y - 1)));
			Vector3 v3 = matrix.getTransformed(verticies.get((int) (f.vertex.z - 1)));
			double intersect = intersect(v1, v2, v3, r);
			if (intersect < 0.0)
				continue;

			double intersectCheck = intersect(v2, v3, v1, r);
			if (intersectCheck < 0.0)
				continue;
			double T = intersect;
			if (T < dist && T > 0.0) {
				dist = T;
				hit = r.pointOnRay(dist);
				normal = getNormal(f);
				isHit = true;
			}

		}

		return new Intersection(isHit, hit, normal);

	}

	private double intersect(Vector3 v1, Vector3 v2, Vector3 v3, Ray r) {
		// Moller-Trumbore ray-triangle intersection algorithm

		Vector3 e1 = v2.clone().sub(v1);
		Vector3 e2 = v3.clone().sub(v1);

		Vector3 p = r.getDir().cross(e2);
		double det = e1.dot(p);
		if (Epsilon.nearlyEquals(det, 0.0))
			return -1.0;
		double invDet = 1.0 / det;

		Vector3 t = r.getOrigin().clone().sub(v1);

		double u = t.dot(p) * invDet;
		if (u < 0.0 || u > 1.0)
			return -1.0;
		Vector3 q = t.cross(e1);
		double v = r.getDir().dot(q) * invDet;
		if (v < 0.0 || v > 1.0)
			return -1.0;
		double T = e2.dot(q) * invDet;

		return T;
	}

	public void setMaterial(Material material) {
		this.mat = material;
	}

	/**
	 * Gets the normal {@link Vector3} of a face (or really any triangle).
	 * 
	 * @param v1
	 *            First vertex of the triangle
	 * @param v2
	 *            Second vertex of the triangle
	 * @param r
	 *            Third vertex of the triangle
	 * @return the {@link Vector3} normal to the triangle
	 */
	private Vector3 getNormal(Face f) {
		// TODO implement Phong interpolation
		// return normals.get((int) (f.vertex.x - 1));// temp approximation
		/*
		 * Vector3 v1 = matrix.getTransformed(verticies.get((int) (f.vertex.x -
		 * 1))); Vector3 v2 = matrix.getTransformed(verticies.get((int)
		 * (f.vertex.y - 1))); Vector3 v3 =
		 * matrix.getTransformed(verticies.get((int) (f.vertex.z - 1)));
		 * 
		 * Vector3 e1 = v2.clone().sub(v1); Vector3 e2 = v3.clone().sub(v1);
		 * 
		 * return e1.cross(e2);
		 */
		return this.normals.get((int) (f.normal.x - 1));
	}

	public void translate(Vector3 v) {
		matrix.translate(v);
		boundingSphere.translate(v);

	}

	public void rotateX(double theta, boolean aroundOrigin) {
		matrix.rotateX(theta, aroundOrigin);
	}

	public void rotateY(double theta, boolean aroundOrigin) {
		matrix.rotateY(theta, aroundOrigin);
	}

	public void rotateZ(double theta, boolean aroundOrigin) {
		matrix.rotateZ(theta, aroundOrigin);
	}

	public Model clone() {
		Model m = new Model();
		m.verticies = (ArrayList<Vector3>) verticies.clone();
		m.normals = (ArrayList<Vector3>) normals.clone();
		m.faces = (ArrayList<Face>) faces.clone();
		m.matrix = matrix.clone();
		m.setMaterial(mat.clone());
		m.boundingSphere = boundingSphere.clone();
		return m;
	}

	public boolean contains(Vector3 v) {
		System.err.println("The 'contains' method is not defined for the class Model.");
		return false;
	}

	public boolean equals(Primitive prim) {
		System.err.println("The 'equals' method is not defined for the class Model.");
		return false;
	}

}
