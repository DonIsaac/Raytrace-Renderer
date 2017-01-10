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

		// test intersection with bounding sphere to improve efficency
		Intersection i = boundingSphere.intersects(r);
		//if (!i.isHit) 
		//	return new Intersection();
		

		Vector3 hit = Vector3.ZERO.clone(), normal = Vector3.ZERO.clone();
		double dist = Double.MAX_VALUE;
		boolean isHit = false;
		// Moller-Trumbore ray-triangle intersection algorithm
		// TODO make this shit work lmao
		for (Face f: faces) {
			Vector3 v1 = matrix.getTransformed(verticies.get((int) (f.vertex.x - 1)));
			Vector3 v2 = matrix.getTransformed(verticies.get((int) (f.vertex.y - 1)));
			Vector3 v3 = matrix.getTransformed(verticies.get((int) (f.vertex.z - 1)));

			Vector3 e1 = v2.clone().sub(v1);
			Vector3 e2 = v3.clone().sub(v1);

			Vector3 p = r.getDir().cross(e2);
			double det = e1.dot(p);
			if (Epsilon.nearlyEquals(det, 0.0))
				continue;
			double invDet = 1.0 / det;

			Vector3 t = r.getOrigin().clone().sub(v1);

			double u = t.dot(p) * invDet;
			if (u < 0.0 || u > 1.0)
				continue;
			Vector3 q = t.cross(e1);
			double v = r.getDir().dot(q) * invDet;
			if (v < 0.0 || v > 1.0)
				continue;
			double T = e2.dot(q) * invDet;
			if (T < dist && T>0.0) {
				dist = T;
				hit = r.pointOnRay(dist);
				normal = getNormal(f);
				isHit = true;
			}

		}

		return new Intersection(isHit, hit, normal);

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
