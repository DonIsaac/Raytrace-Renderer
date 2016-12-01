package model;

import geometry.Plane;
import geometry.Vector3;
import lighting.Material;

public class PlaneModel extends Plane implements IModel{
	private Material m;
	public PlaneModel(Vector3 point, Vector3 normal, Material material) {
		super(point, normal);
		this.m = material;
		// TODO Auto-generated constructor stub
	}
	public PlaneModel(Vector3 v1, Vector3 v2, Vector3 v3){
		super(v1,v2,v3);
	}

	public PlaneModel clone(){
		return new PlaneModel(p.clone(),n.clone(),m.clone());
	}
	public Material getMaterial() {
		return m;
	}
	public void setMaterial(Material material) {
		this.m=material;
		
	}
}
