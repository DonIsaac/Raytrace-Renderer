package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import geometry.Sphere;
import geometry.Transform;
import geometry.Vector3;
import lighting.Material;

public class ModelLoader {

	public static Model loadObjModel(File file, Material material) throws IOException, FileNotFoundException{
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
		m.boundingSphere = new Sphere(Vector3.ZERO.clone(), radius);
		m.matrix = Transform.getIdentityInstance();
		return m;
	}
}
