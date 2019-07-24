package render.strategies;

import java.awt.Color;

import geometry.Ray;
import geometry.Vector3;
import lighting.Light;
import lighting.Material;
import model.IModel;
import render.Camera;
import scene.Scene;
import tools.RaycastHit;

public class PhongStrategy extends RenderStrategy {

	public PhongStrategy() {
		this.name = "phong";
	}

	@Override
	public Vector3 render(Camera cam, Scene s, Ray ray, RaycastHit hit) {
		Vector3 c = new Vector3(0.0, 0.0, 0.0);
		IModel h = hit.itemHit;
		Color itemColor = h.getMaterial().getColor();
		Material m = h.getMaterial();
		Vector3 n = hit.normal.clone();
		c.x = (double) itemColor.getRed() * s.ambient.getInitialIntensity();
		c.y = (double) itemColor.getGreen() * s.ambient.getInitialIntensity();
		c.z = (double) itemColor.getBlue() * s.ambient.getInitialIntensity();
		Vector3 lookVec = ray.getOrigin().getSubtract(hit.hitPoint).getNormalized();
		for (Light l : s.lights) {

			Ray r = Ray.createRayFromPoints(hit.hitPoint, l.getPos());
			RaycastHit hitToLight = cam.raycast(r, s);

			//check if hitpoint is in a shadow
			if (hitToLight.isHit && hitToLight.dist > .00001) 
				 continue;
			Vector3 refVec = n.getScale(2 * r.getDir().dot(n)).getSubtract(r.getDir());
			// diffuse
			double d = max(r.getDir().dot(n), 0);

			c.x += m.Kd() * (double) itemColor.getRed() * d;
			c.y += m.Kd() * (double) itemColor.getGreen() * d;
			c.z += m.Kd() * (double) itemColor.getBlue() * d;
			// specular
			d = Math.pow(max(0, refVec.dot(lookVec)), hit.itemHit.getMaterial().getAlpha());
			c.x += m.Ks() * (double) l.getColor().getRed() * d;
			c.y += m.Ks() * (double) l.getColor().getGreen() * d;
			c.z += m.Ks() * (double) l.getColor().getBlue() * d;
		}
		c.x = clamp(c.x, 0.0, 255.0);
		c.y = clamp(c.y, 0.0, 255.0);
		c.z = clamp(c.z, 0.0, 255.0);
		return c;
	}

}
