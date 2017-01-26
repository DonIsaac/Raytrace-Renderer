package model;

import java.util.ArrayList;

import geometry.Sphere;
import geometry.Transform;
import geometry.Vector3;
import lighting.Material;

public class Model {
	protected Material mat;
	protected ArrayList<Vector3> verticies = new ArrayList<Vector3>();
	protected ArrayList<Vector3> normals = new ArrayList<Vector3>();
	protected ArrayList<Face> faces = new ArrayList<Face>();
	public Sphere boundingSphere;

}
