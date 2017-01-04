package model;

import geometry.Vector3;

public class Face {
	public Vector3 vertex;
	public Vector3 normal;

	public Face(Vector3 vertex, Vector3 normal) {
		this.vertex = vertex;
		this.normal = normal;
	}

}
