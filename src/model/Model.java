package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import geometry.Intersection;
import geometry.Primitive;
import geometry.Ray;
import geometry.Sphere;
import geometry.Transform;
import geometry.Triangle;
import geometry.Vector3;
import lighting.Material;
/**
 * Re
 * @author Donny
 *
 */
public class Model implements IModel {

	private Model() {

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
		if (!boundingSphere.intersects(r).isHit)
			return new Intersection(false);
		else {
			Intersection intersect = new Intersection(false);
			for (Face f : faces) {
				Triangle temp = new Triangle(verticies.get((int) f.vertex.x-1), verticies.get((int) f.vertex.y-1),
						verticies.get((int) f.vertex.z-1));
				Intersection newInt = temp.intersects(r);
				if (r.getOrigin().distFrom(intersect.getClosestIntersection(r.getOrigin())) > r.getOrigin()
						.distFrom(newInt.getClosestIntersection(r.getOrigin()))) {
					intersect = newInt;
				}
			}
			return intersect;
		}
	}

	public Vector3 getNormal(Vector3 p) {
		System.err.println("Cannot use this method in Model.");
		return null;
	}

	public void setMaterial(Material material) {
		this.mat = material;
	}

	public static Model loadModel(File file, Material material) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Model m = new Model();
		double radius = 0.0;
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("v ")) {
				double x = Double.valueOf(line.split(" ")[1]);
				double y = Double.valueOf(line.split(" ")[2]);
				double z = Double.valueOf(line.split(" ")[3]);
				Vector3 vertex = new Vector3(x, y, z);
				m.verticies.add(vertex);
				if (vertex.len() > radius)
					radius = vertex.len();
			} else if (line.startsWith("vn ")) {
				double x = Double.valueOf(line.split(" ")[1]);
				double y = Double.valueOf(line.split(" ")[2]);
				double z = Double.valueOf(line.split(" ")[3]);
				m.normals.add(new Vector3(x, y, z).getNormalized());
			} else if (line.startsWith("f ")) {
				double vertexIndex1 = Double.valueOf(line.split(" ")[1].split("/")[0]);
				double vertexIndex2 = Double.valueOf(line.split(" ")[2].split("/")[0]);
				double vertexIndex3 = Double.valueOf(line.split(" ")[3].split("/")[0]);
				Vector3 vertexIndicies = new Vector3(vertexIndex1, vertexIndex2, vertexIndex3);

				double normalIndex1 = Double.valueOf(line.split(" ")[1].split("/")[2]);
				double normalIndex2 = Double.valueOf(line.split(" ")[2].split("/")[2]);
				double normalIndex3 = Double.valueOf(line.split(" ")[3].split("/")[2]);
				Vector3 normalIndicies = new Vector3(normalIndex1, normalIndex2, normalIndex3);
				m.faces.add(new Face(vertexIndicies, normalIndicies));
			}
		}
		reader.close();
		m.mat = material;
		m.boundingSphere = new Sphere(Vector3.ZERO, radius);
		m.matrix = Transform.getIdentityInstance();
		return m;
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
