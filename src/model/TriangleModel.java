package model;

import geometry.Triangle;
import geometry.Vector3;
import lighting.Material;
import lighting.Renderable;

public class TriangleModel extends Triangle implements IModel{
	private Material m;
	public TriangleModel(Vector3 p, Vector3 q, Vector3 t, Material material) {
		super(p, q, t);
		this.m=material;
	}

	public Material getMaterial() {
		return m;
	}

	public void setMaterial(Material material) {
		this.m=material;
		
	}

	public TriangleModel clone(){
		return new TriangleModel(v1.clone(),v2.clone(),v3.clone(),m.clone());
	}
}
