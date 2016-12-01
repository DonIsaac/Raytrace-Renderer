package model;

import geometry.Sphere;
import geometry.Vector3;
import lighting.Material;

public class SphereModel extends Sphere implements IModel {
	private Material m;

	public SphereModel(Vector3 pos, double radius, Material material) {
		super(pos, radius);
		this.m = material;
	}

	public SphereModel clone() {
		return new SphereModel(c.clone(), r, m.clone());
	}

	public Material getMaterial() {
		return m;
	}

	public void setMaterial(Material material) {
		this.m = material;

	}
}
