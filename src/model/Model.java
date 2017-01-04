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
		Vector3 hit = Vector3.ZERO, normal = Vector3.ZERO;
		double dist = -1.0;
		boolean isHit = false;
		
		//test intersection with bounding sphere to improve efficency
		if (!boundingSphere.intersects(r).isHit)
			return new Intersection();
		
		else {
			//Moller-Trumbore ray-triangle intersection algorithm
			for (Face f : faces) {
				Vector3 v1 = verticies.get((int) (f.vertex.x - 1));
				Vector3 v2 = verticies.get((int) (f.vertex.x - 2));
				Vector3 v3 = verticies.get((int) (f.vertex.x - 3));

				Vector3 e1 = v2.getSubtract(v1);
				Vector3 e2 = v3.getSubtract(v1);

				Vector3 p = r.getDir().cross(e2);
				double det = e1.dot(p);

				if (Epsilon.nearlyEquals(det, 0.0))
					break;

				double invDet = 1.0 / det;

				Vector3 t = r.getOrigin().getSubtract(v1);

				double u = t.dot(p) * invDet;
				if (u < 0.0 || u > 1.0)
					break;

				Vector3 q = t.cross(e1);
				double v = r.getDir().dot(q) * invDet;
				if (v < 0.0 || v > 1.0)
					break;

				double T = e2.dot(q) * invDet;
				if (T > dist) {
					dist = T;
					hit = r.pointOnRay(dist);
					normal = getNormal(v1, v2, v3);
				}

			}

			return new Intersection(isHit, hit, normal);

		}

	}

	public void setMaterial(Material material) {
		this.mat = material;
	}
	/**
	 * Gets the normal {@link Vector3} of a face (or really any triangle).
	 * @param p First vertex of the triangle
	 * @param q Second vertex of the triangle
	 * @param r Third vertex of the triangle
	 * @return the {@link Vector3} normal to the triangle
	 */
	private Vector3 getNormal(Vector3 p, Vector3 q, Vector3 r) {
		//Phong interpolation
		throw new UnsupportedOperationException();
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
